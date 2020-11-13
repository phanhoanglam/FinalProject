package com.spring.Final.modules.job_category_employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobCategoryEmployeeRepository extends JpaRepository<JobCategoryEmployeeEntity, Integer> {
}
