package com.spring.Final.modules.employee.projections;

import com.spring.Final.modules.job_category.projections.NameWithJobCount;
import com.spring.Final.modules.skill.projections.Skill;
import lombok.Data;

import java.util.List;

@Data
public class ProfilePageData {
    private EmployeeProfile employeeProfile;
    private List<Skill> skills;
    private List<NameWithJobCount> jobCategories;

    public ProfilePageData(EmployeeProfile employeeProfile, List<Skill> skills, List<NameWithJobCount> jobCategories) {
        this.employeeProfile = employeeProfile;
        this.skills = skills;
        this.jobCategories = jobCategories;
    }
}
