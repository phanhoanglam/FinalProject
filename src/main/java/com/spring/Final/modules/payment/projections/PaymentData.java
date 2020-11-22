package com.spring.Final.modules.payment.projections;

import com.spring.Final.modules.employer.projections.Membership;
import com.spring.Final.modules.job_proposal.projections.Employer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentData {
    private int id;
    private Employer employer;
    private Membership membership;
    private float VATRate;
    private BigDecimal amount;
    private Date createdAt;

    private String orderId;

    public String getOrderId() {
        return String.format("%1$" + 5 + "s", this.id).replace(' ', '0');
    }

    public BigDecimal getOriginalAmount() {
        return this.amount.divide(new BigDecimal(Float.toString(this.VATRate / 100 + 1)));
    }

    public BigDecimal getVATAmount() {
        return this.amount.subtract(this.getOriginalAmount());
    }

    public String VARRateToString() {
        return String.format("%.0f", this.VATRate);
    }
}
