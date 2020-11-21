package com.spring.Final.modules.page;

import com.spring.Final.modules.employee.EmployeeEntity;
import com.spring.Final.modules.employee.EmployeeService;
import com.spring.Final.modules.employer.EmployerEntity;
import com.spring.Final.modules.employer.EmployerService;
import com.spring.Final.modules.job_proposal.JobProposalService;
import com.spring.Final.modules.jobs.JobService;
import com.spring.Final.modules.page.projections.DashboardData;
import com.spring.Final.modules.review.ReviewService;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageService {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobProposalService jobProposalService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployerService employerService;

    public DashboardData getDashboardData(int userId, String role) {
        DashboardData dashboardData = new DashboardData();

        if (role.equals("employee")) {
            EmployeeEntity employee = this.employeeService.getOne(userId);
            long reviewsCount = this.reviewService.countReviewsByUser(userId, UserType.EMPLOYEE);
            long jobProposalsSuccessCount = this.jobProposalService.countJobDone(userId);

            dashboardData.setRating(employee.getRating());
            dashboardData.setReviewsCount(reviewsCount);
            dashboardData.setJobProposalsSuccessCount(jobProposalsSuccessCount);
        } else {
            EmployerEntity employer = this.employerService.getOne(userId);
            long reviewsCount = this.reviewService.countReviewsByUser(userId, UserType.EMPLOYER);
            long jobsCount = this.jobService.countByEmployer(userId);

            dashboardData.setRating(employer.getRating());
            dashboardData.setReviewsCount(reviewsCount);
            dashboardData.setJobsCount(jobsCount);
        }

        return dashboardData;
    }
}
