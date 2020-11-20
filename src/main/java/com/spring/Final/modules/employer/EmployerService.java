package com.spring.Final.modules.employer;

import com.spring.Final.core.exceptions.EntityExistException;
import com.spring.Final.core.exceptions.ResourceNotFoundException;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.auth.dtos.RegisterDTO;
import com.spring.Final.modules.employer.projections.EmployerDetail;
import com.spring.Final.modules.employer.projections.EmployerDetailData;
import com.spring.Final.modules.employer.projections.EmployerList;
import com.spring.Final.modules.job_proposal.JobProposalService;
import com.spring.Final.modules.job_proposal.projections.JobProposalDetailExistence;
import com.spring.Final.modules.job_proposal.projections.JobProposalDetailExistence2;
import com.spring.Final.modules.jobs.JobService;
import com.spring.Final.modules.jobs.projections.JobManage;
import com.spring.Final.modules.membership.MembershipEntity;
import com.spring.Final.modules.review.ReviewService;
import com.spring.Final.modules.shared.enums.membership_duration.MembershipDuration;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployerService extends ApiService<EmployerEntity, EmployerRepository> {
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JobService jobService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JobProposalService jobProposalService;

    public EmployerService(
            EmployerRepository repository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public PageImpl<EmployerList> list(int pageNumber, int size) {
        Pageable page = PageRequest.of(this.getPage(pageNumber), size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<EmployerEntity> results = this.repository.findAll(page);
        List<EmployerList> resultList = results.stream()
                .map(e -> this.modelMapper.map(e, EmployerList.class))
                .collect(Collectors.toList());

        return new PageImpl<>(resultList, results.getPageable(), results.getTotalElements());
    }

    public EmployerDetailData getDetail(String slug, int employeeId) {
        EmployerEntity employer = this.repository.findBySlug(slug);

        if (employer == null) {
            throw new ResourceNotFoundException();
        }
        JobProposalDetailExistence2 jobProposal = this.jobProposalService.findByEmployerAndEmployee(employer.getId(), employeeId);

        return new EmployerDetailData(
                this.modelMapper.map(employer, EmployerDetail.class),
                this.jobService.listByEmployer(employer),
                this.reviewService.listByUser(employer.getId(), UserType.EMPLOYER),
                jobProposal != null
        );
    }

    public EmployerEntity register(RegisterDTO data) {
        EmployerEntity newEmployer = this.modelMapper.map(data, EmployerEntity.class);
        EmployerEntity employer = this.repository.findByEmail(newEmployer.getEmail());

        if (employer != null) {
            throw new EntityExistException("This email is already used");
        }
        newEmployer.setPassword(this.passwordEncoder.encode(newEmployer.getPassword()));
        newEmployer.setAddressLocation(CommonHelper.createGeometryPoint(data.getAddressLocation()));
        newEmployer.setSlug(CommonHelper.toSlug(newEmployer.getEmail()));
        newEmployer.setCreatedAt(CommonHelper.getCurrentTime());
        newEmployer.setUpdatedAt(CommonHelper.getCurrentTime());
        newEmployer.setVerified(true);

        return this.repository.save(newEmployer);
    }

    public void increaseJobsCount(int id) {
        this.repository.increaseJobsCount(id);
    }

    public void updateMembership(int id, MembershipEntity membership) {
        Timestamp expiredAt = CommonHelper.getCurrentTime();
        MembershipDuration duration = membership.getDuration();

        if (duration == MembershipDuration.MONTHLY) {
            expiredAt.setTime(expiredAt.getTime() + 30L * 24 * 60 * 60 * 1000);
        } else if (duration == MembershipDuration.YEARLY) {
            expiredAt.setTime(expiredAt.getTime() + 365L * 24 * 60 * 60 * 1000);
        }
        EmployerEntity employer = this.repository.getOne(id);
        employer.setMembership(membership);
        employer.setMembershipEndAt(expiredAt);
        this.repository.save(employer);
    }

    public Page<JobManage> listJobs(int page, int size, int id) {
        EmployerEntity employer = this.repository.getOne(id);

        return this.jobService.listByEmployer(page, size, employer);
    }

    public void updateRating(int id, float rating) {
        EmployerEntity employer = this.repository.findById(id).orElse(null);

        if (employer != null) {
            employer.setRating(rating);
            this.repository.save(employer);
        }
    }
}
