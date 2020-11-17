package com.spring.Final.modules.jobs.projections;

import com.spring.Final.modules.job_proposal.projections.JobProposalList;
import lombok.Data;

import java.util.List;

@Data
public class ManageCandidatesData {
    private JobManage job;
    private List<JobProposalList> list;

    public ManageCandidatesData(JobManage job, List<JobProposalList> list) {
        this.job = job;
        this.list = list;
    }
}
