package com.spring.Final.modules.job_category_employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Final.core.BaseEntity;
import com.spring.Final.modules.employee.EmployeeEntity;
import com.spring.Final.modules.job_category.JobCategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "JobCategoryEmployee")
@Table(name = "job_category_employee")
public class JobCategoryEmployeeEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

    @ManyToOne()
    @JoinColumn(name = "job_category_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private JobCategoryEntity jobCategory;

    @ManyToOne()
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private EmployeeEntity employee;

}
