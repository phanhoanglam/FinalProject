package com.spring.Final.modules.jobs.projections;

import com.spring.Final.modules.shared.enums.job_status.JobStatus;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Data
public class JobDetail {
    private int id;
    private String name;
    private String description;
    private String address;
    private String slug;
    private BigDecimal salaryFrom;
    private BigDecimal salaryTo;
    private HashMap<String, Double> addressLocation;
    private JobStatus status;
    private Date expiredAt;
    private Date createdAt;

    private JobType jobType;
    private JobCategory jobCategory;
    private Employer employer;

    public void setAddressLocation(Point point) {
        if (point != null) {
            this.addressLocation = new HashMap<>();
            this.addressLocation.put("x", point.getX());
            this.addressLocation.put("y", point.getY());
        }
    }
}
