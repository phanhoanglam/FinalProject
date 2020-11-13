package com.spring.Final.modules.review;

import com.spring.Final.core.exceptions.BadRequestException;
import com.spring.Final.core.exceptions.ResourceNotFoundException;
import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.job_proposal.JobProposalEntity;
import com.spring.Final.modules.job_proposal.JobProposalService;
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

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public PageImpl<ReviewList> listByEmployee(int pageNumber, int size, int employeeId) {
        Pageable page = PageRequest.of(this.getPage(pageNumber), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReviewEntity> results = this.repository.findAllByToUserIdAndToUserType(employeeId, UserType.EMPLOYEE, page);
        List<ReviewList> resultList = results.stream()
                .map(e -> this.modelMapper.map(e, ReviewList.class))
                .collect(Collectors.toList());

        return new PageImpl<>(resultList, results.getPageable(), results.getTotalElements());
    }

    @Transactional
    public ReviewList reviewEmployee(ReviewEmployeeDTO data) {
        JobProposalEntity jobProposal = this.jobProposalService.getOne(data.getJobProposalId());

        if (jobProposal == null) {
            throw new ResourceNotFoundException();
        } else if (jobProposal.getStatus() != JobProposalStatus.SUCCEEDED) {
            throw new BadRequestException("Status of job proposal is invalid");
        }
        int userId = jobProposal.getJob().getEmployer().getId();
        UserType userType = UserType.EMPLOYER;

        if (this.repository.findTopByJobProposalAndUserIdAndUserType(jobProposal, userId, userType) != null) {
            throw new ExistingReviewException();
        }
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
        ReviewEntity review = this.modelMapper.map(data, ReviewEntity.class);
        review.setUserId(userId);
        review.setUserType(userType);
        review.setToUserId(jobProposal.getEmployee().getId());
        review.setToUserType(UserType.EMPLOYEE);
        review = this.repository.save(review);

        this.createNotification(review, jobProposal.getJob().getName());

        return this.modelMapper.map(review, ReviewList.class);
    }

    @Transactional
    public ReviewList reviewEmployer(ReviewEmployerDTO data) {
        JobProposalEntity jobProposal = this.jobProposalService.getOne(data.getJobProposalId());

        if (jobProposal == null) {
            throw new ResourceNotFoundException();
        } else if (jobProposal.getStatus() == JobProposalStatus.PENDING || jobProposal.getStatus() == JobProposalStatus.REJECTED) {
            throw new BadRequestException("Status of job proposal is invalid");
        }
        int userId = jobProposal.getEmployee().getId();
        UserType userType = UserType.EMPLOYEE;

        if (this.repository.findTopByJobProposalAndUserIdAndUserType(jobProposal, userId, userType) != null) {
            throw new ExistingReviewException();
        }
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
        ReviewEntity review = this.modelMapper.map(data, ReviewEntity.class);

        review.setUserId(userId);
        review.setUserType(userType);
        review.setToUserId(jobProposal.getJob().getEmployer().getId());
        review.setToUserType(UserType.EMPLOYER);
        review = this.repository.save(review);

        this.createNotification(review, jobProposal.getJob().getName());

        return this.modelMapper.map(review, ReviewList.class);
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
}
