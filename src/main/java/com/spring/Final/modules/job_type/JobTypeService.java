package com.spring.Final.modules.job_type;

import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.shared.dtos.NameOnly;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobTypeService extends ApiService<JobTypeEntity, JobTypeRepository> {
    public JobTypeService(JobTypeRepository repository) {
        this.repository = repository;
    }

    public List<NameOnly> findAllAsNameOnly() {
        return this.repository.findAllAsNameOnly();
    }
}
