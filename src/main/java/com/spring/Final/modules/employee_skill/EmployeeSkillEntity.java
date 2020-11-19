package com.spring.Final.modules.employee_skill;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.modules.employee.EmployeeEntity;
import com.spring.Final.modules.skill.SkillEntity;
import lombok.*;

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
    @EqualsAndHashCode.Exclude @ToString.Exclude
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude @ToString.Exclude
    @JoinColumn(name = "skill_id", referencedColumnName = "id", nullable = false)
    private SkillEntity skill;
}
