package com.spring.Final.modules.employee_skill;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.modules.employee.EmployeeEntity;
import com.spring.Final.modules.skill.SkillEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "EmployeeSkill")
@Table(name = "employee_skill")
public class EmployeeSkillEntity extends BaseEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", referencedColumnName = "id", nullable = false)
    private SkillEntity skill;
}
