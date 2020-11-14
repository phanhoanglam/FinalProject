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
    private float rating;
    private int minHourlyRate;
    private String slug;
}
