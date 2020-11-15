package com.spring.Final.modules.jobs.projections;

import com.spring.Final.modules.job_category.projections.NameWithJobCount;
import com.spring.Final.modules.shared.dtos.NameOnly;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Data
public class HomepageData<T> {
    private List<NameWithJobCount> jobCategories;
    private List<NameWithJobCount> jobTypes;
    private Page<NameOnly> skills;
    PageImpl<T> list;

    public HomepageData(List<NameWithJobCount> jobCategories, List<NameWithJobCount> jobTypes, Page<NameOnly> skills, PageImpl<T> list) {
        this.jobCategories = jobCategories;
        this.jobTypes = jobTypes;
        this.skills = skills;
        this.list = list;
    }
}
