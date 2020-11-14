package com.spring.Final.modules.shared.enums.payment_status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PaymentStatusAttributeConverter implements AttributeConverter<PaymentStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PaymentStatus userType) {
        if (userType == null) {
            return null;
        }
        switch (userType) {
            case requires_payment_method:
                return 1;
            case requires_confirmation:
                return 2;
            case requires_action:
                return 3;
            case processing:
                return 4;
            case requires_capture:
                return 5;
            case canceled:
                return 6;
            case succeeded:
                return 7;
            default:
                throw new IllegalArgumentException(userType + " not supported!");
        }
    }

    @Override
    public PaymentStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData) {
            case 1:
                return PaymentStatus.requires_payment_method;
            case 2:
                return PaymentStatus.requires_confirmation;
            case 3:
                return PaymentStatus.requires_action;
            case 4:
                return PaymentStatus.processing;
            case 5:
                return PaymentStatus.requires_capture;
            case 6:
                return PaymentStatus.canceled;
            case 7:
                return PaymentStatus.succeeded;
            default:
                throw new IllegalArgumentException(dbData + " not supported!");
        }
    }

    public static PaymentStatus convertFromStripeStatus(String stripeStatus) {
        switch (stripeStatus) {
            case "requires_payment_method":
                return PaymentStatus.requires_payment_method;
            case "requires_confirmation":
                return PaymentStatus.requires_confirmation;
            case "requires_action":
                return PaymentStatus.requires_action;
            case "processing":
                return PaymentStatus.processing;
            case "requires_capture":
                return PaymentStatus.requires_capture;
            case "canceled":
                return PaymentStatus.canceled;
            case "succeeded":
                return PaymentStatus.succeeded;
            default:
                throw new IllegalArgumentException(stripeStatus + " not supported!");
        }
    }
}

