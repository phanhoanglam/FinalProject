package com.spring.Final.modules.jobs.projections;

import lombok.Data;

import java.util.Date;

@Data
public class EmployerJobList {
    private int id;
    private String name;
    private String address;
    private String slug;
    private Date createdAt;

    private JobType jobType;
}
