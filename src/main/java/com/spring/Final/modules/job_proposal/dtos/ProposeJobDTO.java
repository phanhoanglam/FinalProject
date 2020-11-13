package com.spring.Final.modules.job_proposal.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;

@Data
public class ProposeJobDTO implements Serializable {
	@NotNull(message = "Job is required")
	private int jobId;

	private int employeeId;

	private HashMap<String, Object> attachments;

	private String message;
}
