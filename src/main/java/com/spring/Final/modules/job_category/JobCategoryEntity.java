package com.spring.Final.modules.job_category;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.modules.employee.EmployeeEntity;
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
@Entity(name = "JobCategory")
@Table(name = "job_categories")
public class JobCategoryEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "varchar(255) default 'Hello world world world world'")
    private String description;

    @Column(name = "icon_class", nullable = false, columnDefinition = "varchar(255) default 'icon-line-awesome-file-code-o'")
    private String iconClass;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = CommonHelper.getCurrentTime();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "job_category_employee",
            joinColumns = @JoinColumn(name = "job_category_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<EmployeeEntity> employees;

    public JobCategoryEntity(String name, String description, String iconClass) {
        this.name = name;
        this.description = description;
        this.iconClass = iconClass;
    }
}
