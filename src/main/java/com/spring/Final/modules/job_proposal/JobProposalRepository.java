package com.spring.Final.modules.job_proposal;

import com.spring.Final.modules.job_proposal.projections.EmploymentHistory;
import com.spring.Final.modules.jobs.JobEntity;
import com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobProposalRepository extends JpaRepository<JobProposalEntity, Integer> {
    @Query(value = "select jp from JobProposal jp where jp.status in ?1 and jp.employee.id = ?2 and jp.job.id = ?3")
    Page<JobProposalEntity> findByStatusEmployeeJob(Pageable pageable, JobProposalStatus[] statuses, int employeeId, int jobId);

    List<JobProposalEntity> findAllByJobAndStatusIn(JobEntity job, JobProposalStatus[] status, Sort sort);

    @Modifying
    @Query(value = "delete from JobProposal jp where jp.job.id = ?1")
    void deleteByJobId(int jobId);

    @Query(value = "select new com.spring.Final.modules.job_proposal.projections.EmploymentHistory(" +
                "jp.id," +
                "jp.job.name," +
                "jp.job.employer.name," +
                "jp.job.employer.avatar," +
                "jp.startedAt," +
                "jp.endedAt" +
            ") from JobProposal jp where jp.employee.id = ?1 and jp.status in (" +
            "com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus.ACCEPTED," +
            "com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus.FAILED," +
            "com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus.SUCCEEDED)")
    List<EmploymentHistory> findEmploymentHistory(int employeeId);

    @Query("select count(jp) from JobProposal jp where jp.job.id = ?1 and jp.status <> com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus.REJECTED")
    long countByJob(int jobId);
}
