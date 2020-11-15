package com.spring.Final.modules.jobs.projections;

import com.spring.Final.modules.shared.enums.job_status.JobStatus;
import lombok.Data;

import java.util.Date;

@Data
public class JobManage {
    private int id;
    private String name;
    private JobStatus status;
    private String slug;

    private Date expiredAt;
    private Date createdAt;

    private long candidatesCount;
}
