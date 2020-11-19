package com.spring.Final.modules.review;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.modules.job_proposal.JobProposalEntity;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import com.spring.Final.modules.shared.enums.user_type.UserTypeAttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Review")
@Table(name = "reviews")
public class ReviewEntity extends BaseEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_proposal_id", referencedColumnName = "id", nullable = false)
    private JobProposalEntity jobProposal;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "user_type", nullable = false)
    @Convert(converter = UserTypeAttributeConverter.class)
    private UserType userType;

    @Column(name = "to_user_id", nullable = false)
    private Integer toUserId;

    @Column(name = "to_user_type", nullable = false)
    @Convert(converter = UserTypeAttributeConverter.class)
    private UserType toUserType;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "delivered_on_budget", nullable = false)
    private boolean deliveredOnBudget;

    @Column(name = "delivered_on_time", nullable = false)
    private boolean deliveredOnTime;

    @Column(name = "rating", nullable = false, columnDefinition = "float default 0")
    private float rating;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

}
