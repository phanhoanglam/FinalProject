package com.spring.Final.modules.review.projections;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewList {
    private int id;
    private JobProposal jobProposal;
    private String message;
    private Boolean deliveredOnBudget;
    private Boolean deliveredOnTime;
    private float rating;
    private Date createdAt;
}

@Data
class JobProposal {
    private int id;
    private String name;
    private Job job;
}

@Data
class Job {
    private int id;
    private String name;
}
