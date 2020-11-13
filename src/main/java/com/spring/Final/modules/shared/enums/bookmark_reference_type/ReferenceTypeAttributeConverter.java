package com.spring.Final.modules.shared.enums.bookmark_reference_type;

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
            case FREELANCER:
                return 1;
            case EMPLOYER:
                return 2;
            case JOB:
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
                return ReferenceType.FREELANCER;
            case 2:
                return ReferenceType.EMPLOYER;
            case 3:
                return ReferenceType.JOB;
            default:
                throw new IllegalArgumentException(dbData + " not supported!");
        }
    }
}
