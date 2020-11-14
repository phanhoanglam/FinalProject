package com.spring.Final.modules.shared.enums.user_type;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UserTypeAttributeConverter implements AttributeConverter<UserType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserType userType) {
        if (userType == null) {
            return null;
        }

        switch (userType) {
            case EMPLOYEE:
                return 1;
            case EMPLOYER:
                return 2;
            default:
                throw new IllegalArgumentException(userType + " not supported!");
        }
    }

    @Override
    public UserType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        switch (dbData) {
            case 1:
                return UserType.EMPLOYEE;
            case 2:
                return UserType.EMPLOYER;
            default:
                throw new IllegalArgumentException(dbData + " not supported!");
        }

    }

}

