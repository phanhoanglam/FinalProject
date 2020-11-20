package com.spring.Final.modules.job_proposal.projections;

import lombok.Data;

@Data
public class Job {
    private int id;

    private String name;

    private Employer employer;
}

