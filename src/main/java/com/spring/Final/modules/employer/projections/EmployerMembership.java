package com.spring.Final.modules.employer.projections;

import com.spring.Final.modules.shared.enums.membership_type.MembershipType;
import lombok.Data;

import java.util.Date;

@Data
public class EmployerMembership {
    private Membership membership;
    private Date membershipEndAt;
}
