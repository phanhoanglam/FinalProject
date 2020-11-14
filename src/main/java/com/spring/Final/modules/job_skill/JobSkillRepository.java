package com.spring.Final.modules.job_skill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSkillRepository extends JpaRepository<JobSkillEntity, Integer> {
}
