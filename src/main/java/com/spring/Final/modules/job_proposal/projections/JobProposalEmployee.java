package com.spring.Final.modules.job_proposal.projections;

import com.spring.Final.modules.employer.EmployerEntity;
import com.spring.Final.modules.jobs.projections.JobDetail;
import com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus;
import com.spring.Final.modules.shared.enums.job_proposal_type.JobProposalType;
import com.spring.Final.modules.shared.enums.job_status.JobStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class JobProposalEmployee {
    private int id;
    private JobDetail job;
    private JobProposalType type;
    private JobProposalStatus status;
    private Date startedAt;
    private Date endedAt;
    private Date createdAt;
    private boolean hasReview;
}
