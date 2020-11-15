package com.spring.Final.modules.jobs.projections;

import com.spring.Final.modules.job_category.projections.NameWithJobCount;
import com.spring.Final.modules.jobs.dtos.JobDTO;
import com.spring.Final.modules.shared.dtos.NameOnly;
import com.spring.Final.modules.shared.enums.job_status.JobStatus;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PostJobData {
    private List<NameWithJobCount> jobCategories;
    private List<NameOnly> jobTypes;
    private Page<NameOnly> skills;
    private JobDTO job;
    private JobStatus[] status;

    public PostJobData(List<NameWithJobCount> jobCategories, List<NameOnly> jobTypes, Page<NameOnly> skills) {
        this.jobCategories = jobCategories;
        this.jobTypes = jobTypes;
        this.skills = skills;
    }

    public PostJobData(List<NameWithJobCount> jobCategories, List<NameOnly> jobTypes, Page<NameOnly> skills, JobDTO job, JobStatus[] status) {
        this.jobCategories = jobCategories;
        this.jobTypes = jobTypes;
        this.skills = skills;
        this.job = job;
        this.status = status;
    }
}
