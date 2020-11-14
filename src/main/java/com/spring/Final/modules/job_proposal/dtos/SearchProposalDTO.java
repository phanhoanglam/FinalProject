package com.spring.Final.modules.job_proposal.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SearchProposalDTO {
    @NotNull(message = "Job is required")
	private int jobId;

	private int employeeId;
}
