package com.spring.Final.modules.employer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.modules.membership.MembershipEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Employer")
@Table(name = "employers")
public class EmployerEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

//    @OneToMany(mappedBy = "employer_id")
//    private Set<JobEntity> jobs;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id", referencedColumnName = "id")
    private MembershipEntity membership;

    @Column(name = "facebook_id")
    private String facebookId;

    @Column(name = "google_id")
    private String googleId;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "phone", length = 30, nullable = false)
    private String phone;

    @Column(name = "avatar", nullable = false)
    private String avatar = "http://www.vasterad.com/themes/hireo/images/company-logo-05.png";

    @Column(name = "address")
    private String address;

    @Column(name = "address_location", columnDefinition = "geometry")
    private Point addressLocation;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "rating", nullable = false, columnDefinition = "float default 0")
    private float rating;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

//    @Column(name = "is_valid_employer", nullable = false)
//    private boolean isValidEmployer = false;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "jobs_count", nullable = false)
    @ColumnDefault("0")
    private int jobsCount = 0;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_expire")
    @Temporal(TemporalType.DATE)
    private Date verificationExpire;

    @Column(name = "reset_password_code")
    private String resetPasswordCode;

    @Column(name = "reset_password_expire")
    @Temporal(TemporalType.DATE)
    private Date resetPasswordExpire;

    @Column(name = "social_profiles")
    private String socialProfiles;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked = false;

    @Column(name = "membership_end_at")
    @Temporal(TemporalType.DATE)
    private Date membershipEndAt;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = CommonHelper.getCurrentTime();

}
