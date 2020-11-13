package com.spring.Final.modules.payment.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CreatePaymentDTO implements Serializable {
    @NotNull(message = "Membership is required")
    private int membershipId;

    private int employerId;
}
