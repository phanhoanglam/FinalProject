package com.spring.Final.modules.employee.projections;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.util.HashMap;

@Data
public class EmployeeDetail {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String avatar;
    private String address;
    private HashMap<String, Double> addressLocation;
    private String nationality;
    private String description;
    private float rating;
    private int minHourlyRate;
    private String attachments;
    private String slug;

    public void setAddressLocation(Point point) {
        if (point != null) {
            this.addressLocation = new HashMap<>();
            this.addressLocation.put("x", point.getX());
            this.addressLocation.put("y", point.getY());
        }
    }
}