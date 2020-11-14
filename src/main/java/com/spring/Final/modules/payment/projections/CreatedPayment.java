package com.spring.Final.modules.payment.projections;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CreatedPayment {
    private int id;
    private BigDecimal amount;
    private String secret;
    private Date createdAt;
}
