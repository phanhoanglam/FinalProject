package com.spring.Final.modules.shared.enums.membership_duration;

import javax.persistence.AttributeConverter;

public class MembershipDurationAttributeConverter implements AttributeConverter<MembershipDuration, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MembershipDuration membershipDuration) {
        if (membershipDuration == null) {
            return null;
        }
        switch (membershipDuration) {
            case MONTHLY:
                return 1;
            case YEARLY:
                return 2;
            default:
                throw new IllegalArgumentException(membershipDuration + " not supported!");
        }
    }

    @Override
    public MembershipDuration convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData) {
            case 1:
                return MembershipDuration.MONTHLY;
            case 2:
                return MembershipDuration.YEARLY;
            default:
                throw new IllegalArgumentException(dbData + " not supported!");
        }
    }
}
