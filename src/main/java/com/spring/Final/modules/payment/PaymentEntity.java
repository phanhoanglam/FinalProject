package com.spring.Final.modules.payment;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.modules.employer.EmployerEntity;
import com.spring.Final.modules.membership.MembershipEntity;
import com.spring.Final.modules.shared.enums.payment_status.PaymentStatus;
import com.spring.Final.modules.shared.enums.payment_status.PaymentStatusAttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Payment")
@Table(name = "payments")
public class PaymentEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", referencedColumnName = "id", nullable = false)
    private EmployerEntity employer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id", referencedColumnName = "id", nullable = false)
    private MembershipEntity membership;

    @Column(name = "stripe_id", nullable = false)
    private String stripeId;

    @Column(name = "amount", columnDefinition = "decimal", scale = 2)
    private BigDecimal amount;

    @Column(name = "status", nullable = false)
    @Convert(converter = PaymentStatusAttributeConverter.class)
    private PaymentStatus status;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = CommonHelper.getCurrentTime();
}
