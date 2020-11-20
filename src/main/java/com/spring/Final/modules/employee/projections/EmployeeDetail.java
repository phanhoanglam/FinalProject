package com.spring.Final.modules.employee.projections;

import com.spring.Final.modules.job_proposal.projections.EmploymentHistory;
import com.spring.Final.modules.skill.SkillEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmployeeDetail {
    private int id;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String avatar;
    private String nationality;
    private String description;
    private String socialProfiles;
    private String attachments;
    private float rating;
    private int minHourlyRate;
    private long jobHiredCount;
    private long jobDoneCount;
    private long jobDoneOnTime;
    private long jobDoneOnBudget;
    private List<EmploymentHistory> employmentHistory;
    private List<Skill> skills = new ArrayList<>();

    private int successRate;
    private int jobDoneOnTimePercentage;
    private int jobDoneOnBudgetPercentage;

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }

    public void setJobDoneOnTime(long jobDoneOnTime) {
        this.jobDoneOnTime = jobDoneOnTime;

        if (this.jobHiredCount > 0) {
            this.jobDoneOnTimePercentage = (int) ((float) jobDoneOnTime / this.jobHiredCount * 100);
        }
    }

    public void setJobDoneOnBudget(long jobDoneOnBudget) {
        this.jobDoneOnBudget = jobDoneOnBudget;

        if (this.jobHiredCount > 0) {
            this.jobDoneOnBudgetPercentage = (int) ((float) jobDoneOnBudget / this.jobHiredCount * 100);
        }
    }
}

@Data
class Skill {
    private String name;
}
