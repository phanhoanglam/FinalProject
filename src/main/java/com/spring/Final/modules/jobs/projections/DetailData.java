package com.spring.Final.modules.jobs.projections;

import com.spring.Final.modules.job_proposal.projections.JobProposalDetailExistence;
import lombok.Data;

import java.util.List;

@Data
public class DetailData {
    private JobDetail jobDetails;
    private JobProposalDetailExistence proposal;
    private List<JobList> jobLists;

    public DetailData(JobDetail jobDetails, JobProposalDetailExistence proposal, List<JobList> jobLists) {
        this.jobDetails = jobDetails;
        this.proposal = proposal;
        this.jobLists = jobLists;
    }
}
