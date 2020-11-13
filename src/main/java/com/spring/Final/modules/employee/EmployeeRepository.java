package com.spring.Final.modules.employee;

import com.spring.Final.modules.employee.projections.EmployeeGetDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer>, JpaSpecificationExecutor<EmployeeEntity> {
	EmployeeEntity findByEmail(String email);

	@Query(value = "select new com.spring.Final.modules.employee.projections.EmployeeGetDetail(" +
			"e.id, " +
			"e.firstName, " +
			"e.lastName, " +
			"e.jobTitle, " +
			"e.avatar, " +
			"e.nationality, " +
			"e.description, " +
			"e.socialProfiles, " +
			"e.rating, " +
			"e.minHourlyRate, " +
			"(select count(jp) from JobProposal jp where jp.status in (" +
				"com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus.ACCEPTED," +
				"com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus.FAILED," +
				"com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus.SUCCEEDED)), " +
			"(select count(jp) from JobProposal jp where jp.status = com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus.SUCCEEDED)) " +
			"from Employee e where e.slug = ?1")
	EmployeeGetDetail findBySlug(String slug);
}
