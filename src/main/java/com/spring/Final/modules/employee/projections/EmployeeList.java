package com.spring.Final.modules.employee.projections;

import lombok.Data;

@Data
public class EmployeeList {
    private int id;
    private String firstName;
    private String lastName;
    private String avatar;
    private String address;
    private String nationality;
    private String jobTitle;
    private float rating;
    private int successRate;
    private int minHourlyRate;
    private String slug;
}
