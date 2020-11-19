package com.spring.Final.modules.employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.core.helpers.PointSerializer;
import com.spring.Final.modules.skill.SkillEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Employee")
@Table(name = "employees")
public class EmployeeEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

    @Column(name = "facebook_id", length = 20)
    private String facebookId;

    @Column(name = "google_id", length = 20)
    private String googleId;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "phone", length = 30, nullable = false)
    private String phone;

    @Column(name = "avatar", nullable = false)
    private String avatar = "https://media.defense.gov/2020/Feb/19/2002251686/700/465/0/200219-A-QY194-002.JPG";

    @Column(name = "address")
    private String address;

    @Column(name = "address_location", columnDefinition = "geometry")
    @JsonSerialize(using = PointSerializer.class)
    private Point addressLocation;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "job_title", columnDefinition = "varchar(255) default 'Developer'")
    private String jobTitle;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "rating", nullable = false)
    private float rating = 0;

    @Column(name = "min_hourly_rate", nullable = false)
    private int minHourlyRate = 0;

    @Column(name = "attachments")
    private String attachments;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_expire")
    private Date verificationExpire;

    @Column(name = "reset_password_code")
    private String resetPasswordCode;

    @Column(name = "reset_password_expire")
    private Date resetPasswordExpire;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked = false;

    @Column(name = "created_at")
    private Date createdAt = CommonHelper.getCurrentTime();

    @Column(name = "updated_at")
    private Date updatedAt = CommonHelper.getCurrentTime();

    @Column(name = "social_profiles")
    private String socialProfiles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "employee_skill",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<SkillEntity> skills;

//    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
//    private Set<JobProposalEntity> jobProposals;
//
//    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
//    private Set<JobCategoryEmployeeEntity> jobCategoriesEmployees;
//
//    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
//    private Set<EmployeeSkillEntity> employeeSkills;

//    @JsonIgnore
//    public Set<JobProposalEntity> getJobProposals() {
//        return jobProposals;
//    }
//
//	@JsonIgnore
//	public Set<JobCategoryEmployeeEntity> getJobCategoriesEmployees() {
//		return jobCategoriesEmployees;
//	}
//
//	@JsonIgnore
//	public Set<EmployeeSkillEntity> getEmployeeSkills() {
//		return employeeSkills;
//	}
}
