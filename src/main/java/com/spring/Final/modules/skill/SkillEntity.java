package com.spring.Final.modules.skill;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.modules.employee.EmployeeEntity;
import com.spring.Final.modules.job_category.JobCategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Skill")
@Table(name = "skills")
public class SkillEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id", referencedColumnName = "id", nullable = false) // allow null for existing entities
    private JobCategoryEntity jobCategory;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = CommonHelper.getCurrentTime();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "employee_skill",
            joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<EmployeeEntity> employees;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "job_skill",
            joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id"))
    private List<EmployeeEntity> jobs;

    public SkillEntity(String name, JobCategoryEntity jobCategory) {
        this.name = name;
        this.jobCategory = jobCategory;
    }
}
