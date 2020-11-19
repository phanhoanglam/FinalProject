package com.spring.Final.modules.employer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRepository extends JpaRepository<EmployerEntity, Integer> {
	EmployerEntity findByEmail(String email);

	EmployerEntity findBySlug(String slug);

	@Modifying
	@Query(value = "update Employer as e set e.jobsCount = e.jobsCount + 1 where e.id = ?1")
	void increaseJobsCount(int id);
}
