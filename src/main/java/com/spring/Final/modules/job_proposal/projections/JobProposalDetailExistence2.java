package com.spring.Final.modules.job_proposal.projections;

import com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus;
import com.spring.Final.modules.shared.enums.job_proposal_type.JobProposalType;
import lombok.Data;

import java.util.Date;

@Data
public class JobProposalDetailExistence2 {
    private int id;

    private String message;

    private JobProposalStatus status;

    private JobProposalType type;

    private Date createdAt;

    public Employee employee;

    public Job job;
}
