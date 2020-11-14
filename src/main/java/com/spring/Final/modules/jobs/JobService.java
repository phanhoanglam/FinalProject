package com.spring.Final.modules.jobs;

import com.spring.Final.core.exceptions.BadRequestException;
import com.spring.Final.core.exceptions.ResourceNotFoundException;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.employer.EmployerEntity;
import com.spring.Final.modules.employer.EmployerService;
import com.spring.Final.modules.job_category.JobCategoryService;
import com.spring.Final.modules.job_proposal.JobProposalService;
import com.spring.Final.modules.job_proposal.projections.JobProposalList;
import com.spring.Final.modules.job_type.JobTypeService;
import com.spring.Final.modules.jobs.dtos.JobDTO;
import com.spring.Final.modules.jobs.dtos.SearchJobDTO;
import com.spring.Final.modules.jobs.exceptions.ExceedFreeJobsAvailableException;
import com.spring.Final.modules.jobs.projections.JobDetail;
import com.spring.Final.modules.jobs.projections.JobList;
import com.spring.Final.modules.jobs.projections.JobManage;
import com.spring.Final.modules.jobs.specifications.JobSpecification;
import com.spring.Final.modules.shared.enums.job_status.JobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService extends ApiService<JobEntity, JobRepository> {
    @Autowired
    private EmployerService employerService;

    @Autowired
    private JobTypeService jobTypeService;

    @Autowired
    private JobCategoryService jobCategoryService;

    @Autowired
    private JobProposalService jobProposalService;

    private final long EXPIRED_DAYS = 60;
    private final int FREE_JOBS = 100;

    public JobService(JobRepository repository) {
        this.repository = repository;
    }

    public PageImpl<JobList> list(int pageNumber, int size, SearchJobDTO model) {
        Pageable page = PageRequest.of(this.getPage(pageNumber), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        JobSpecification search = new JobSpecification(model);

        Page<JobEntity> results = this.repository.findAll(search, page);
        List<JobList> resultList = results.stream()
                .map(e -> this.modelMapper.map(e, JobList.class))
                .collect(Collectors.toList());

        return new PageImpl<>(resultList, results.getPageable(), results.getTotalElements());
    }

    public JobDetail getDetail(String slug) {
        JobEntity data = this.repository.findBySlug(slug);

        if (data == null) {
            throw new ResourceNotFoundException();
        }

        return this.modelMapper.map(data, JobDetail.class);
    }

    public List<JobProposalList> listProposals(String slug) {
        JobEntity data = this.repository.findBySlug(slug);

        if (data == null) {
            throw new ResourceNotFoundException();
        }

        return this.jobProposalService.listByJob(data);
    }

    @Transactional
    public JobDetail createOne(JobDTO data) {
        EmployerEntity employer = this.employerService.getOne(data.getEmployerId());

        if (employer.getJobsCount() >= this.FREE_JOBS) {
            throw new ExceedFreeJobsAvailableException();
        }
        JobEntity job = convertToEntity(data);
        this.employerService.increaseJobsCount(data.getEmployerId());
        job = this.repository.save(job);

        return this.modelMapper.map(job, JobDetail.class);
    }

    public JobDetail updateOne(JobDTO data) {
        JobEntity job = convertToEntity(data);
        job = this.repository.save(job);

        return this.modelMapper.map(job, JobDetail.class);
    }

    @Transactional
    public void deleteOne(int id) {
        JobEntity job = this.repository.findById(id).orElse(null);

        if (job == null) {
            throw new ResourceNotFoundException();
        }
        // TODO: Create notifications to the proposals of the deleted job
        this.jobProposalService.deleteByJobId(id);
        this.repository.deleteById(id);
    }

    public Page<JobManage> listByEmployer(int pageNumber, int size, EmployerEntity employer) {
        Pageable page = PageRequest.of(this.getPage(pageNumber), size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<JobEntity> results = this.repository.findAllByEmployer(employer, page);
        List<JobManage> resultList = results.stream()
                .map(e -> this.modelMapper.map(e, JobManage.class))
                .collect(Collectors.toList());
        for (JobManage jobManage : resultList) {
            jobManage.setCandidatesCount(this.jobProposalService.countJobCandidates(jobManage.getId()));
        }

        return new PageImpl<>(resultList, results.getPageable(), results.getTotalElements());
    }

    private JobEntity convertToEntity(JobDTO data) {
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
        JobEntity job = this.modelMapper.map(data, JobEntity.class);

        job.setJobType(this.jobTypeService.getOne(data.getJobTypeId()));
        job.setEmployer(this.employerService.getOne(data.getEmployerId()));
        job.setJobCategory(this.jobCategoryService.getOne(data.getJobCategoryId()));
        job.setAddressLocation(CommonHelper.createGeometryPoint(data.getAddressLocation()));
        job.getAddressLocation().setSRID(4326);
        job.setUpdatedAt(CommonHelper.getCurrentTime());

        if (data.getId() != 0) {
            JobEntity oldJob = this.repository.getOne(data.getId());

            if (oldJob.getStatus() == JobStatus.CLOSED) {
                throw new BadRequestException("Job was already closed, cannot edit!");
            }
            job.setEmployer(oldJob.getEmployer());
            job.setExpiredAt(oldJob.getExpiredAt());
            job.setSlug(oldJob.getSlug());
            job.setCreatedAt(oldJob.getCreatedAt());
        } else {
            String slug = CommonHelper.toSlug(data.getName()) + "-" + CommonHelper.getAlphaNumericString(6);
            Timestamp expiredAt = CommonHelper.getCurrentTime();
            expiredAt.setTime(expiredAt.getTime() + EXPIRED_DAYS * 24 * 60 * 60 * 1000);

            job.setSlug(slug);
            job.setStatus(JobStatus.OPEN);
            job.setExpiredAt(expiredAt);
            job.setCreatedAt(CommonHelper.getCurrentTime());
        }

        return job;
    }
}