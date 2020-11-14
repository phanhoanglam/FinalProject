package com.spring.Final.modules.membership.projections;

import com.spring.Final.modules.shared.enums.membership_duration.MembershipDuration;
import com.spring.Final.modules.shared.enums.membership_type.MembershipType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MembershipDetail {
    private int id;
    private String name;
    private BigDecimal price;
    private MembershipType type;
    private MembershipDuration duration;
    private String description;
}
