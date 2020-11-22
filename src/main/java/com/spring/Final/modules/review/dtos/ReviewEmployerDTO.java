package com.spring.Final.modules.review.dtos;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
public class ReviewEmployerDTO implements Serializable {
    private int employerId;

    @Positive
    @NotNull(message = "Job proposal is required")
    private int jobProposalId;

    @Min(1)
    @Max(5)
    @NotNull(message = "Rating is required")
    private float rating;

    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Message is required")
    private String message;
}
