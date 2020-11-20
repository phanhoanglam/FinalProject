package com.spring.Final.modules.employee.projections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.core.helpers.PointSerializer;
import com.spring.Final.modules.employee_skill.EmployeeSkillEntity;
import com.spring.Final.modules.shared.dtos.NameOnly;
import com.spring.Final.modules.skill.SkillService;
import com.spring.Final.modules.skill.projections.Skill;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class EmployeeProfile implements Serializable {
    private int id;
    private String email;
    private String firstName;
    private String password;
    private String lastName;
    private String phone;
    private String avatar;
    private String address;
    private String nationality;
    private String jobTitle;
    private String description;
    private int minHourlyRate;
    private String attachments;
    private String socialProfiles;
    private List<Integer> skillIds = new ArrayList<>();
}
