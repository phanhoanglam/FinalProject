package com.spring.Final.modules.jobs;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.common.MapUtils;
import com.spring.Final.core.exceptions.BadRequestException;
import com.spring.Final.core.exceptions.ResourceNotFoundException;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.employee.EmployeeEntity;
import com.spring.Final.modules.employee.EmployeeService;
import com.spring.Final.modules.employer.EmployerEntity;
import com.spring.Final.modules.employer.EmployerService;
import com.spring.Final.modules.job_category.JobCategoryService;
import com.spring.Final.modules.job_proposal.JobProposalService;
import com.spring.Final.modules.job_proposal.dtos.SearchProposalDTO;
import com.spring.Final.modules.job_proposal.projections.Employee;
import com.spring.Final.modules.job_proposal.projections.JobProposalDetailExistence;
import com.spring.Final.modules.job_type.JobTypeService;
import com.spring.Final.modules.jobs.dtos.JobDTO;
import com.spring.Final.modules.jobs.dtos.SearchJobDTO;
import com.spring.Final.modules.jobs.exceptions.ExceedFreeJobsAvailableException;
import com.spring.Final.modules.jobs.projections.*;
import com.spring.Final.modules.jobs.specifications.JobSpecification;
import com.spring.Final.modules.shared.enums.job_status.JobStatus;
import com.spring.Final.modules.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService extends ApiService<JobEntity, JobRepository> {
    @Autowired
    private EmployerService employerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JobTypeService jobTypeService;

    @Autowired
    private JobCategoryService jobCategoryService;

    @Autowired
    private JobProposalService jobProposalService;

    @Autowired
    private SkillService skillService;

    private final long EXPIRED_DAYS = 60;
    private final int FREE_JOBS = 100;

    public JobService(JobRepository repository) {
        this.repository = repository;
    }

    public HomepageData list(int pageNumber, int size, SearchJobDTO model) {
        Pageable page = PageRequest.of(this.getPage(pageNumber), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        JobSpecification search = new JobSpecification(model);

        Page<JobEntity> results = this.repository.findAll(search, page);
        List<JobList> resultList = results.stream()
                .map(e -> this.modelMapper.map(e, JobList.class))
                .collect(Collectors.toList());

        return new HomepageData(
                jobCategoryService.findAllAsNameOnly(),
                jobTypeService.findAllAsNameOnly(),
                skillService.findAllAsNameOnly(1, 100, model.getJobCategories().stream().mapToInt(i -> i).toArray()),
                new PageImpl<>(resultList, results.getPageable(), results.getTotalElements())
        );
    }

    public HomepageData listMaps(int pageNumber, int size, SearchJobDTO model, int uid) {
        Pageable page = PageRequest.of(this.getPage(pageNumber), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        EmployeeEntity employeeEntity = employeeService.getOne(uid);
        model.setLocation(employeeEntity.getAddress());
        JobSpecification search = new JobSpecification(model);
        Page<JobEntity> results = this.repository.findAll(search, page);
        List<JobList> resultList = results.stream()
                .map(e -> this.modelMapper.map(e, JobList.class))
                .collect(Collectors.toList());

        return new HomepageData(
                jobCategoryService.findAllAsNameOnly(),
                jobTypeService.findAllAsNameOnly(),
                skillService.findAllAsNameOnly(1, 100, model.getJobCategories().stream().mapToInt(i -> i).toArray()),
                new PageImpl<>(resultList, results.getPageable(), results.getTotalElements())
        );
    }

    public DetailData getDetail(String slug, Authentication authentication) {
        JobEntity data = this.repository.findBySlug(slug);
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int id = (int) user.getInformation().get("id");

        if (data == null) {
            throw new ResourceNotFoundException();
        }

        JobDetail viewModel = this.modelMapper.map(data, JobDetail.class);

        Pageable page = PageRequest.of(this.getPage(1), 2, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<JobEntity> listEntity = this.repository.findAllByIdNotAndJobCategory(data.getId(), data.getJobCategory(), page);
        List<JobList> resultList = listEntity.stream()
                .map(e -> this.modelMapper.map(e, JobList.class))
                .collect(Collectors.toList());

        SearchProposalDTO search = new SearchProposalDTO();
        search.setJobId(viewModel.getId());
        search.setEmployeeId(id);
        JobProposalDetailExistence proposal = jobProposalService.searchProposal(search);

        return new DetailData(
                viewModel,
                proposal,
                resultList
        );
    }

    public PostJobData getPostJobData(int jobCategoryId) {
        return new PostJobData(
                jobCategoryService.findAllAsNameOnly(),
                jobTypeService.findAllAsNameOnly(),
                skillService.findAllAsNameOnly(1, 100, new int[]{jobCategoryId})
        );
    }

    @Transactional
    public JobDetail createOne(JobDTO data) throws IOException {
        EmployerEntity employer = this.employerService.getOne(data.getEmployerId());

        if (employer.getJobsCount() >= this.FREE_JOBS) {
            throw new ExceedFreeJobsAvailableException();
        }
        JobEntity job = convertToEntity(data);
        this.employerService.increaseJobsCount(data.getEmployerId());
        job = this.repository.save(job);

        return this.modelMapper.map(job, JobDetail.class);
    }

    public PostJobData getEditJobData(String slug, int jobCategoryId) {
        JobEntity data = this.repository.findBySlug(slug);

        if (data == null) {
            throw new ResourceNotFoundException();
        }
        jobCategoryId = jobCategoryId == 0 ? data.getJobCategory().getId() : jobCategoryId;
        JobDTO jobDTO = this.modelMapper.map(data, JobDTO.class);
        jobDTO.setSkillIds(data.getSkills().stream().map(BaseEntity::getId).collect(Collectors.toList()));

        return new PostJobData(
                jobCategoryService.findAllAsNameOnly(),
                jobTypeService.findAllAsNameOnly(),
                skillService.findAllAsNameOnly(1, 100, new int[]{jobCategoryId}),
                jobDTO,
                new JobStatus[]{JobStatus.OPEN, JobStatus.PROGRESSING, JobStatus.CLOSED}
        );
    }

    @Transactional
    public JobDetail updateOne(JobDTO data) throws IOException {
        JobEntity job = convertToEntity(data);

        if (job.getStatus() == JobStatus.CLOSED) {
            this.jobProposalService.setStatusSucceeded(job);
        }
        job = this.repository.save(job);

        return this.modelMapper.map(job, JobDetail.class);
    }

    @Transactional
    public void deleteOne(String slug) {
        JobEntity job = this.repository.findBySlug(slug);

        if (job == null) {
            throw new ResourceNotFoundException();
        } else if (job.getStatus() == JobStatus.CLOSED) {
            throw new BadRequestException("Job was already closed, cannot delete!");
        }
        // TODO: Create notifications to the proposals of the deleted job
        this.jobProposalService.deleteByJobId(job.getId());
        this.repository.deleteById(job.getId());
    }

    public List<EmployerJobList> listByEmployer(EmployerEntity employer) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        List<JobEntity> results = this.repository.findAllByEmployer(employer, sort);

        return results.stream().map(e -> this.modelMapper.map(e, EmployerJobList.class)).collect(Collectors.toList());
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

    public ManageCandidatesData listProposals(String slug) {
        JobEntity data = this.repository.findBySlug(slug);

        if (data == null) {
            throw new ResourceNotFoundException();
        }
        JobManage jobManage = this.modelMapper.map(data, JobManage.class);
        jobManage.setCandidatesCount(this.jobProposalService.countJobCandidates(jobManage.getId()));

        return new ManageCandidatesData(jobManage, this.jobProposalService.listByJob(data));
    }

    private JobEntity convertToEntity(JobDTO data) throws IOException {
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
        JobEntity job = this.modelMapper.map(data, JobEntity.class);

        job.setJobType(this.jobTypeService.getOne(data.getJobTypeId()));
        job.setEmployer(this.employerService.getOne(data.getEmployerId()));
        job.setJobCategory(this.jobCategoryService.getOne(data.getJobCategoryId()));
        job.setSkills(this.skillService.getAllByIds(data.getSkillIds()));
        job.setAddressLocation(CommonHelper.createGeometryPoint(MapUtils.getCoordinateByText(data.getAddress())));
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
