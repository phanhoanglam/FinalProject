package com.spring.Final.modules.job_proposal.projections;

import com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus;
import com.spring.Final.modules.shared.enums.job_proposal_type.JobProposalType;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class JobProposalList {
    private int id;

    private String message;

    private Map<String, Object> attachments;

    private JobProposalStatus status;

    private JobProposalType type;

    private boolean hasReview;

    private Date createdAt;

    private Employee employee;
}
