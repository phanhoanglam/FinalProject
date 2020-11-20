package com.spring.Final.modules.employer.projections;

import com.spring.Final.modules.jobs.projections.EmployerJobList;
import com.spring.Final.modules.review.projections.ReviewList;
import lombok.Data;

import java.util.List;

@Data
public class EmployerDetailData {
    private EmployerDetail employerDetail;

    private List<EmployerJobList> jobList;

    private List<ReviewList> reviewList;

    private boolean allowReview;

    public EmployerDetailData(
            EmployerDetail employeeDetail,
            List<EmployerJobList> jobList,
            List<ReviewList> reviewList,
            boolean allowReview
    ) {
        this.employerDetail = employeeDetail;
        this.jobList = jobList;
        this.reviewList = reviewList;
        this.allowReview = allowReview;
    }
}
