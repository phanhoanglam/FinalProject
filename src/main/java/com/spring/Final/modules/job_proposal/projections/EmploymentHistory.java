package com.spring.Final.modules.job_proposal.projections;

import lombok.Data;

import java.util.Date;

@Data
public class EmploymentHistory {
    private int id;
    private String jobName;
    private String employerName;
    private String employerAvatar;
    private String employerSlug;
    private Date startedAt;
    private Date endedAt;

    public EmploymentHistory(int id, String jobName, String employerName, String employerAvatar, String employerSlug, Date startedAt, Date endedAt) {
        this.id = id;
        this.jobName = jobName;
        this.employerName = employerName;
        this.employerAvatar = employerAvatar;
        this.employerSlug = employerSlug;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }
}
