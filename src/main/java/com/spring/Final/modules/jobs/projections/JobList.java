package com.spring.Final.modules.jobs.projections;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Data
public class JobList {
    private int id;
    private String name;
    private String address;
    private String slug;

    private BigDecimal salaryFrom;
    private BigDecimal salaryTo;
    private HashMap<String, Double> addressLocation;
    private String location;
    private Date createdAt;

    private JobType jobType;
    private Employer employer;

    public void setAddressLocation(org.locationtech.jts.geom.Point point) {
        if (point != null) {
            this.addressLocation = new HashMap<>();
            this.addressLocation.put("x", point.getX());
            this.addressLocation.put("y", point.getY());
            this.location = point.getX() + "," + point.getY();
        }
    }

    public String getAddressLocation() {
        return addressLocation.get("x") + "," + addressLocation.get("y");
    }
}

@Data
class JobType {
    private int id;
    private String name;
}

@Data
class JobCategory {
    private int id;
    private String name;
}

@Data
class Employer {
    private int id;
    private String name;
    private String email;
    private String avatar;
    private String address;
    private String nationality;
    private String phone;
    private float rating;
    private String slug;
}
