package com.spring.Final.modules.job_category;

import com.spring.Final.modules.job_category.projections.NameWithJobCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategoryEntity, Integer> {
    @Query(value = "select new com.spring.Final.modules.job_category.projections.NameWithJobCount(" +
            "f.id, " +
            "f.name, " +
            "(select count(j) from Job j where j.jobCategory.id = f.id), " +
            "f.description, " +
            "f.iconClass) " +
            "from JobCategory f")
    List<NameWithJobCount> findAllAsNameOnly();
}
