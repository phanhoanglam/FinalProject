package com.spring.Final.modules.employee.projections;

import com.spring.Final.modules.job_proposal.projections.EmploymentHistory;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeGetDetail {
    private int id;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String avatar;
    private String nationality;
    private String description;
    private String socialProfiles;
    private float rating;
    private int minHourlyRate;
    private long jobHiredCount;
    private long jobDoneCount;
    private List<EmploymentHistory> employmentHistory;

    public EmployeeGetDetail(int id, String firstName, String lastName, String jobTitle, String avatar, String nationality, String description, String socialProfiles, float rating, int minHourlyRate, long jobHiredCount, long jobDoneCount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.avatar = avatar;
        this.nationality = nationality;
        this.description = description;
        this.socialProfiles = socialProfiles;
        this.rating = rating;
        this.minHourlyRate = minHourlyRate;
        this.jobHiredCount = jobHiredCount;
        this.jobDoneCount = jobDoneCount;
    }
}
