package com.spring.Final.modules.shared.enums.notification_reference_type;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ReferenceTypeAttributeConverter implements AttributeConverter<ReferenceType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ReferenceType referenceType) {
        if (referenceType == null) {
            return null;
        }

        switch (referenceType) {
            case JOB:
                return 1;
            case JOB_PROPOSAL:
                return 2;
            case REVIEW:
                return 3;
            default:
                throw new IllegalArgumentException(referenceType + " not supported!");
        }
    }

    @Override
    public ReferenceType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        switch (dbData) {
            case 1:
                return ReferenceType.JOB;
            case 2:
                return ReferenceType.JOB_PROPOSAL;
            case 3:
                return ReferenceType.REVIEW;
            default:
                throw new IllegalArgumentException(dbData + " not supported!");
        }
    }
}
