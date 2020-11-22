package com.spring.Final.modules.review.projections;

import com.spring.Final.modules.shared.enums.user_type.UserType;
import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String avatar;
    private String slug;
    private String nationality;
    private UserType type;
}
