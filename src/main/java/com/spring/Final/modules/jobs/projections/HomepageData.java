package com.spring.Final.modules.jobs.projections;

import com.spring.Final.modules.job_category.projections.NameWithJobCount;
import com.spring.Final.modules.shared.dtos.NameOnly;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Data
public class HomepageData {
    private List<NameWithJobCount> jobCategories;
    private List<NameOnly> jobTypes;
    private Page<NameOnly> skills;
    PageImpl<JobList> list;

    public HomepageData(List<NameWithJobCount> jobCategories, List<NameOnly> jobTypes, Page<NameOnly> skills, PageImpl<JobList> list) {
        this.jobCategories = jobCategories;
        this.jobTypes = jobTypes;
        this.skills = skills;
        this.list = list;
    }
}
