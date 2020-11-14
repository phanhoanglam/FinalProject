package com.spring.Final.modules.jobs.projections;

import lombok.Data;

import java.util.Date;

@Data
public class JobManage {
    private int id;
    private String name;
    private String slug;

    private Date expiredAt;
    private Date createdAt;

    private long candidatesCount;
}
