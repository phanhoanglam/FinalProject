package com.spring.Final.modules.shared.enums.payment_type;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PaymentTypeAttributeConverter implements AttributeConverter<PaymentType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PaymentType userType) {
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
    public PaymentType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData) {
            case 1:
                return PaymentType.requires_payment_method;
            case 2:
                return PaymentType.requires_confirmation;
            case 3:
                return PaymentType.requires_action;
            case 4:
                return PaymentType.processing;
            case 5:
                return PaymentType.requires_capture;
            case 6:
                return PaymentType.canceled;
            case 7:
                return PaymentType.succeeded;
            default:
                throw new IllegalArgumentException(dbData + " not supported!");
        }
    }

    public static PaymentType convertFromStripeStatus(String stripeStatus) {
        switch (stripeStatus) {
            case "requires_payment_method":
                return PaymentType.requires_payment_method;
            case "requires_confirmation":
                return PaymentType.requires_confirmation;
            case "requires_action":
                return PaymentType.requires_action;
            case "processing":
                return PaymentType.processing;
            case "requires_capture":
                return PaymentType.requires_capture;
            case "canceled":
                return PaymentType.canceled;
            case "succeeded":
                return PaymentType.succeeded;
            default:
                throw new IllegalArgumentException(stripeStatus + " not supported!");
        }
    }
}

