package com.spring.Final.modules.jobs;

import com.spring.Final.modules.employer.EmployerEntity;
import com.spring.Final.modules.job_category.JobCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Integer>, JpaSpecificationExecutor<JobEntity> {
    JobEntity findBySlug(String slug);

    Page<JobEntity> findAllByEmployer(EmployerEntity employer, Pageable pageable);

    List<JobEntity> findAllByEmployer(EmployerEntity employer, Sort sort);

    Page<JobEntity> findAllByIdNotAndJobCategory(int id, JobCategoryEntity jobCategory, Pageable pageable);

    @Query(value = "select count(j) from Job j where j.employer.id = ?1")
    long countByEmployer(int employerId);
}
