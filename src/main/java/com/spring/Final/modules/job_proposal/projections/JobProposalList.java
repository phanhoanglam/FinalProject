package com.spring.Final.modules.job_proposal.projections;

import com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus;
import com.spring.Final.modules.shared.enums.job_proposal_type.JobProposalType;
import lombok.Data;

import java.util.Date;

@Data
public class JobProposalList {
    private int id;

    private String message;

    private JobProposalStatus status;

    private JobProposalType type;

    private Date createdAt;

    private Employee employee;
}

@Data
class Employee {
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private String address;

    private String nationality;

    private String phone;

    private float rating;
}