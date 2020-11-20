package com.spring.Final.modules.employee.projections;

import com.spring.Final.modules.shared.specifications.File;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EmployeeProfile implements Serializable {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String newPassword;
    private String phone;
    private String avatar;
    private String address;
    private String nationality;
    private String jobTitle;
    private String description;
    private int minHourlyRate;
    private String attachments;
    private ArrayList<File> attachmentsDecoded = new ArrayList<>();
    private List<Integer> jobCategoryIds = new ArrayList<>();
    private List<Integer> skillIds = new ArrayList<>();
}
