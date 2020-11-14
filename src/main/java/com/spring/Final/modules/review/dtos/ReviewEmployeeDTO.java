package com.spring.Final.modules.review.dtos;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
public class ReviewEmployeeDTO implements Serializable {
    private int employerId;

    @Positive
    @NotNull(message = "Job proposal is required")
	private int jobProposalId;

    @NotNull(message = "\"Delivered on budget\" is required")
    private boolean deliveredOnBudget;

    @NotNull(message = "\"Delivered on time\" is required")
    private boolean deliveredOnTime;

    @Min(1)
    @Max(5)
    @NotNull(message = "Rating is required")
    private float rating;

    @NotNull(message = "Message is required")
    private String message;
}
