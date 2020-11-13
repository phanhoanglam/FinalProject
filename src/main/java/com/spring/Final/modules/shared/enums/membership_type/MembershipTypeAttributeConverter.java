package com.spring.Final.modules.shared.enums.membership_type;

import javax.persistence.AttributeConverter;

public class MembershipTypeAttributeConverter implements AttributeConverter<MembershipType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MembershipType membershipType) {
        if (membershipType == null) {
            return null;
        }
        switch (membershipType) {
            case STANDARD:
                return 1;
            case PLUS:
                return 2;
            case ENTERPRISE:
                return 3;
            default:
                throw new IllegalArgumentException(membershipType + " not supported!");
        }
    }

    @Override
    public MembershipType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData) {
            case 1:
                return MembershipType.STANDARD;
            case 2:
                return MembershipType.PLUS;
            case 3:
                return MembershipType.ENTERPRISE;
            default:
                throw new IllegalArgumentException(dbData + " not supported!");
        }
    }
}
