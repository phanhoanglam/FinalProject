package com.spring.Final.modules.job_proposal;

import com.spring.Final.core.exceptions.BadRequestException;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.employee.EmployeeService;
import com.spring.Final.modules.job_proposal.dtos.ProposeJobDTO;
import com.spring.Final.modules.job_proposal.dtos.SearchProposalDTO;
import com.spring.Final.modules.job_proposal.exceptions.ExistingProposalException;
import com.spring.Final.modules.job_proposal.projections.*;
import com.spring.Final.modules.jobs.JobEntity;
import com.spring.Final.modules.jobs.JobService;
import com.spring.Final.modules.jobs.dtos.SearchJobDTO;
import com.spring.Final.modules.jobs.exceptions.JobInvalidStatusException;
import com.spring.Final.modules.notification.NotificationService;
import com.spring.Final.modules.review.ReviewService;
import com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus;
import com.spring.Final.modules.shared.enums.job_status.JobStatus;
import com.spring.Final.modules.shared.enums.notification_reference_type.ReferenceType;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobProposalService extends ApiService<JobProposalEntity, JobProposalRepository> {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JobService jobService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ReviewService reviewService;

    public JobProposalService(JobProposalRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public JobProposalList proposeJob(ProposeJobDTO data) {
        JobProposalStatus[] findStatuses = new JobProposalStatus[]{JobProposalStatus.ACCEPTED, JobProposalStatus.PENDING};
        Page<JobProposalEntity> existingProposal = this.repository.findByStatusEmployeeJob(PageRequest.of(0, 1), findStatuses, data.getEmployeeId(), data.getJobId());

        if (existingProposal.getTotalElements() != 0) {
            throw new ExistingProposalException();
        }
        JobEntity job = this.jobService.getOne(data.getJobId());

        if (job.getStatus() != JobStatus.OPEN) {
            throw new JobInvalidStatusException("Job was taken by other hunters!");
        }
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
        JobProposalEntity proposal = this.modelMapper.map(data, JobProposalEntity.class);

        proposal.setJob(job);
        proposal.setEmployee(this.employeeService.getOne(data.getEmployeeId()));
        proposal.setCreatedAt(CommonHelper.getCurrentTime());

        if (proposal.getAttachments() != null) {
            String attachments = CommonHelper.toJSON(data.getAttachments());
            proposal.setAttachments(attachments);
        }
        proposal = this.repository.save(proposal);

        return this.modelMapper.map(proposal, JobProposalList.class);
    }

    public PageImpl<JobProposalEmployee> listJobByEmployee(int pageNumber, int size, int uid) {
        Pageable page = PageRequest.of(this.getPage(pageNumber), size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<JobProposalEntity> result = this.repository.findJobProposalByEmployeeId(page, uid);
        List<JobProposalEmployee> resultList = result.stream()
                .map(e -> {
                    JobProposalEmployee jp = this.modelMapper.map(e, JobProposalEmployee.class);
                    jp.setHasReview(this.reviewService.roleHasReview(UserType.EMPLOYEE, e));

                    return jp;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(resultList, result.getPageable(), result.getTotalElements());
    }

    public JobProposalDetailExistence searchProposal(SearchProposalDTO data) {
        JobProposalStatus[] findStatuses = new JobProposalStatus[]{JobProposalStatus.ACCEPTED, JobProposalStatus.PENDING};
        Page<JobProposalEntity> existingProposal = this.repository.findByStatusEmployeeJob(PageRequest.of(0, 1), findStatuses, data.getEmployeeId(), data.getJobId());

        if (existingProposal.getTotalElements() != 0) {
            return this.modelMapper.map(existingProposal.getContent().get(0), JobProposalDetailExistence.class);
        }
        return null;
    }

    public List<JobProposalList> listByJob(JobEntity job) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        List<JobProposalEntity> jobs = this.repository.findAllByJobAndStatusIn(job, new JobProposalStatus[]{
                JobProposalStatus.PENDING,
                JobProposalStatus.ACCEPTED,
                JobProposalStatus.SUCCEEDED,
                JobProposalStatus.FAILED
        }, sort);

        return jobs.stream().map(e -> {
            JobProposalList jobProposal = this.modelMapper.map(e, JobProposalList.class);
            jobProposal.setAttachments((HashMap<String, Object>) CommonHelper.parseJSON(e.getAttachments(), new HashMap<String, Object>()));
            jobProposal.setHasReview(this.reviewService.roleHasReview(UserType.EMPLOYER, e));

            return jobProposal;
        }).collect(Collectors.toList());
    }

    @Transactional
    public JobProposalList accept(int id) {
        JobProposalEntity jobProposal = this.repository.getOne(id);

        if (jobProposal.getStatus() != JobProposalStatus.PENDING) {
            throw new BadRequestException("Invalid status");
        }
        jobProposal.setStartedAt(CommonHelper.getCurrentTime());
        jobProposal.setStatus(JobProposalStatus.ACCEPTED);
        jobProposal = this.repository.save(jobProposal);

        this.notificationService.create(
                jobProposal.getEmployee().getId(),
                UserType.EMPLOYEE,
                jobProposal.getId(),
                ReferenceType.JOB_PROPOSAL,
                "Your proposal to " + jobProposal.getJob().getName() + " was accepted"
        );

        return this.modelMapper.map(jobProposal, JobProposalList.class);
    }

    @Transactional
    public JobProposalList reject(int id) {
        JobProposalEntity jobProposal = this.repository.getOne(id);

        if (jobProposal.getStatus() != JobProposalStatus.PENDING) {
            throw new BadRequestException("Invalid status");
        }
        jobProposal.setStatus(JobProposalStatus.REJECTED);
        jobProposal = this.repository.save(jobProposal);

        this.notificationService.create(
                jobProposal.getEmployee().getId(),
                UserType.EMPLOYEE,
                jobProposal.getId(),
                ReferenceType.JOB_PROPOSAL,
                "Your proposal to " + jobProposal.getJob().getName() + " was rejected"
        );

        return this.modelMapper.map(jobProposal, JobProposalList.class);
    }

    @Transactional
    public JobProposalList setFailed(int id) {
        JobProposalEntity jobProposal = this.repository.getOne(id);

        if (jobProposal.getStatus() != JobProposalStatus.ACCEPTED) {
            throw new BadRequestException("Invalid status");
        }
        jobProposal.setStatus(JobProposalStatus.FAILED);
        jobProposal.setEndedAt(CommonHelper.getCurrentTime());
        jobProposal = this.repository.save(jobProposal);

        this.notificationService.create(
                jobProposal.getEmployee().getId(),
                UserType.EMPLOYEE, jobProposal.getId(),
                ReferenceType.JOB_PROPOSAL,
                "You were removed from " + jobProposal.getJob().getName()
        );

        return this.modelMapper.map(jobProposal, JobProposalList.class);
    }

    @Transactional
    public JobProposalList setSucceeded(int id) {
        JobProposalEntity jobProposal = this.repository.getOne(id);

        if (jobProposal.getStatus() != JobProposalStatus.ACCEPTED) {
            throw new BadRequestException("Invalid status");
        }
        jobProposal.setStatus(JobProposalStatus.SUCCEEDED);
        jobProposal.setEndedAt(CommonHelper.getCurrentTime());
        jobProposal = this.repository.save(jobProposal);

        this.notificationService.create(
                jobProposal.getEmployee().getId(),
                UserType.EMPLOYEE,
                jobProposal.getId(),
                ReferenceType.JOB_PROPOSAL,
                "Your " + jobProposal.getJob().getName() + " has been succeeded"
        );

        return this.modelMapper.map(jobProposal, JobProposalList.class);
    }

    public List<EmploymentHistory> getEmploymentHistory(int employeeId) {
        return this.repository.findEmploymentHistory(employeeId);
    }

    public void deleteByJobId(int jobId) {
        this.repository.deleteByJobId(jobId);
    }

    public long countJobCandidates(int jobId) {
        return this.repository.countByJob(jobId);
    }

    public long countJobDone(int employeeId) {
        return this.repository.countJobDone(employeeId);
    }

    public long countJobHired(int employeeId) {
        return this.repository.countJobHired(employeeId);
    }

    public int calculateSuccessRate(int employeeId) {
        long[] test = this.repository.calculateSuccessRate(employeeId);

        if (test[1] == 0) {
            return 0;
        } else {
            return (int) ((float) test[0] / test[1] * 100);
        }
    }

    public void setStatusSucceeded(JobEntity job) {
        this.repository.setStatusByJobAndStatus(
                job,
                JobProposalStatus.ACCEPTED,
                JobProposalStatus.SUCCEEDED,
                CommonHelper.getCurrentTime()
        );
    }
}
