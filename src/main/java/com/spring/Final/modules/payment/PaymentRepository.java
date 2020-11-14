package com.spring.Final.modules.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
    PaymentEntity findByStripeId(String stripeId);
}
