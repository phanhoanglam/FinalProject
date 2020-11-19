package com.spring.Final.modules.employee.projections;

import com.spring.Final.modules.skill.projections.Skill;
import lombok.Data;

import java.util.List;

@Data
public class ProfilePageData {
    private EmployeeProfile employeeProfile;
    private List<Skill> skills;

    public ProfilePageData(EmployeeProfile employeeProfile, List<Skill> skills) {
        this.employeeProfile = employeeProfile;
        this.skills = skills;
    }
}
