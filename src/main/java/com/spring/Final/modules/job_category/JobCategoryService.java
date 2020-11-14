package com.spring.Final.modules.job_category;

import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.job_category.projections.NameWithJobCount;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class JobCategoryService extends ApiService<JobCategoryEntity, JobCategoryRepository> {
    public JobCategoryService(JobCategoryRepository repository) {
        this.repository = repository;
    }

    public List<NameWithJobCount> findAllAsNameOnly() {
        List<NameWithJobCount> categories = this.repository.findAllAsNameOnly();
        Comparator<NameWithJobCount> compareByJobCount = (NameWithJobCount o1, NameWithJobCount o2)
                -> o2.getJobCount() > o1.getJobCount() ? 1 : -1;
        Collections.sort(categories, compareByJobCount);

        return categories;
    }
}
