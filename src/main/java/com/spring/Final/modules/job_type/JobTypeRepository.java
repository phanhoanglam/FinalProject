package com.spring.Final.modules.job_type;

import com.spring.Final.modules.shared.dtos.NameOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobTypeRepository extends JpaRepository<JobTypeEntity, Integer> {
    @Query(value = "select f from JobType f")
    List<NameOnly> findAllAsNameOnly();
}
