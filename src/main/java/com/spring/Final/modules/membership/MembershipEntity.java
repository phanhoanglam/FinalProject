package com.spring.Final.modules.membership;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.modules.shared.enums.membership_duration.MembershipDuration;
import com.spring.Final.modules.shared.enums.membership_duration.MembershipDurationAttributeConverter;
import com.spring.Final.modules.shared.enums.membership_type.MembershipType;
import com.spring.Final.modules.shared.enums.membership_type.MembershipTypeAttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Membership")
@Table(name = "memberships")
public class MembershipEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", columnDefinition = "decimal", scale = 2)
    private BigDecimal price;

    @Column(name = "type", nullable = false)
    @Convert(converter = MembershipTypeAttributeConverter.class)
    private MembershipType type;

    @Column(name = "duration", nullable = false, columnDefinition = "int default 1")
    @Convert(converter = MembershipDurationAttributeConverter.class)
    private MembershipDuration duration = MembershipDuration.MONTHLY;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = CommonHelper.getCurrentTime();
}
