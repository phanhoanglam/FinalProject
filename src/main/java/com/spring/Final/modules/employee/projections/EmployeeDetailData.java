package com.spring.Final.modules.employee.projections;

import com.spring.Final.modules.job_proposal.projections.EmploymentHistory;
import com.spring.Final.modules.review.projections.ReviewList;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDetailData {
    private EmployeeDetail employeeDetail;

    private List<EmploymentHistory> employmentHistory;

    private List<ReviewList> reviewList;

    public EmployeeDetailData(EmployeeDetail employeeDetail, List<EmploymentHistory> employmentHistory, List<ReviewList> reviewList) {
        this.employeeDetail = employeeDetail;
        this.employmentHistory = employmentHistory;
        this.reviewList = reviewList;
    }
}
