package com.spring.Final.modules.jobs.dtos;

import com.spring.Final.modules.shared.enums.job_status.JobStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;

@Data
public class JobDTO implements Serializable {
    private int id;

    @NotNull(message = "Job category is required")
    private int jobCategoryId;

    @NotNull(message = "Job type is required")
    private int jobTypeId;

    @NotNull(message = "Status is required")
    private JobStatus status;

    private int employerId;

    @NotBlank(message = "Name is required")
    private String name;

    private float salaryFrom;

    private float salaryTo;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Address location is required")
    private HashMap<String, String> addressLocation;
}
