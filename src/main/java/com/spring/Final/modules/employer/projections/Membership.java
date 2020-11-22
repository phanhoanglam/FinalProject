package com.spring.Final.modules.employer.projections;

import com.spring.Final.modules.shared.enums.membership_type.MembershipType;
import lombok.Data;

@Data
public class Membership {
    private String name;

    private MembershipType type;
}
