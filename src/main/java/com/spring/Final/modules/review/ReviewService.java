package com.spring.Final.modules.review;

import com.spring.Final.core.exceptions.BadRequestException;
import com.spring.Final.core.exceptions.ResourceNotFoundException;
import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.employee.EmployeeService;
import com.spring.Final.modules.employer.EmployerService;
import com.spring.Final.modules.job_proposal.JobProposalEntity;
import com.spring.Final.modules.job_proposal.JobProposalService;
import com.spring.Final.modules.jobs.JobEntity;
import com.spring.Final.modules.notification.NotificationService;
import com.spring.Final.modules.review.dtos.ReviewEmployeeDTO;
import com.spring.Final.modules.review.dtos.ReviewEmployerDTO;
import com.spring.Final.modules.review.exceptions.ExistingReviewException;
import com.spring.Final.modules.review.projections.ReviewList;
import com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus;
import com.spring.Final.modules.shared.enums.notification_reference_type.ReferenceType;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService extends ApiService<ReviewEntity, ReviewRepository> {
    @Autowired
    private JobProposalService jobProposalService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployerService employerService;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public List<ReviewList> listByUser(int userId, UserType userType) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<ReviewEntity> results = this.repository.findAllByToUserIdAndToUserType(userId, userType, sort);

        return results.stream().map(e -> this.modelMapper.map(e, ReviewList.class)).collect(Collectors.toList());
    }

    @Transactional
    public ReviewList reviewEmployee(ReviewEmployeeDTO data) {
        JobProposalEntity jobProposal = this.jobProposalService.getOne(data.getJobProposalId());

        if (jobProposal == null) {
            throw new ResourceNotFoundException();
        } else if (jobProposal.getStatus() != JobProposalStatus.SUCCEEDED && jobProposal.getStatus() != JobProposalStatus.FAILED) {
            throw new BadRequestException("Status of job proposal is invalid");
        }
        int userId = jobProposal.getJob().getEmployer().getId();
        int toUserId = jobProposal.getEmployee().getId();
        UserType userType = UserType.EMPLOYER;
        UserType toUserType = UserType.EMPLOYEE;

        if (this.repository.findTopByJobProposalAndUserIdAndUserType(jobProposal, userId, userType) != null) {
            throw new ExistingReviewException();
        }
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
        ReviewEntity review = this.modelMapper.map(data, ReviewEntity.class);
        review.setUserId(userId);
        review.setUserType(userType);
        review.setToUserId(toUserId);
        review.setToUserType(toUserType);
        review = this.repository.save(review);

        this.employeeService.updateRating(
                jobProposal.getEmployee().getId(),
                this.repository.recalculateRating(jobProposal.getEmployee().getId(), UserType.EMPLOYEE)
        );

        this.createNotification(review, jobProposal.getJob().getName());

        return this.modelMapper.map(review, ReviewList.class);
    }

    @Transactional
    public ReviewList reviewEmployer(ReviewEmployerDTO data) {
        JobProposalEntity jobProposal = this.jobProposalService.findByEmployerAndEmployee(data.getEmployerId(), data.getEmployeeId());

        if (jobProposal == null) {
            throw new ResourceNotFoundException();
        }
        int userId = jobProposal.getEmployee().getId();
        int toUserId = jobProposal.getJob().getEmployer().getId();
        UserType userType = UserType.EMPLOYEE;
        UserType toUserType = UserType.EMPLOYER;

        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
        ReviewEntity review = this.modelMapper.map(data, ReviewEntity.class);

        review.setUserId(userId);
        review.setUserType(userType);
        review.setToUserId(toUserId);
        review.setToUserType(toUserType);
        review = this.repository.save(review);

        this.employerService.updateRating(
                jobProposal.getEmployee().getId(),
                this.repository.recalculateRating(toUserId, toUserType)
        );

        this.createNotification(review, jobProposal.getJob().getName());

        return this.modelMapper.map(review, ReviewList.class);
    }

    public boolean roleHasReview(UserType userType, JobProposalEntity jobProposal) {
        ReviewEntity review = this.repository.findByUserTypeAndJobProposal(userType, jobProposal);

        return review != null;
    }

    private void createNotification(ReviewEntity review, String jobName) {
        this.notificationService.create(
                review.getToUserId(),
                review.getToUserType(),
                review.getId(),
                ReferenceType.REVIEW,
                "You got a review on job " + jobName
        );
    }

    public long countJobDoneOnTime(int employeeId) {
        return this.repository.countJobDoneOnTime(employeeId, UserType.EMPLOYEE);
    }

    public long countJobDoneOnBudget(int employeeId) {
        return this.repository.countJobDoneOnBudget(employeeId, UserType.EMPLOYEE);
    }
}
