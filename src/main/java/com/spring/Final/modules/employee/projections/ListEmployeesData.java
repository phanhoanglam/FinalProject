package com.spring.Final.modules.employee.projections;

import com.spring.Final.modules.job_category.projections.NameWithJobCount;
import com.spring.Final.modules.shared.dtos.NameOnly;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Data
public class ListEmployeesData {
    private List<NameWithJobCount> jobCategories;
    private Page<NameOnly> skills;
    PageImpl<EmployeeList> list;

    public ListEmployeesData(List<NameWithJobCount> jobCategories, Page<NameOnly> skills, PageImpl<EmployeeList> list) {
        this.jobCategories = jobCategories;
        this.skills = skills;
        this.list = list;
    }
}
