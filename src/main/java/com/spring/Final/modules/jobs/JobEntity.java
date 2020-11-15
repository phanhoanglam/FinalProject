package com.spring.Final.modules.jobs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.modules.employee.EmployeeEntity;
import com.spring.Final.modules.employer.EmployerEntity;
import com.spring.Final.modules.job_category.JobCategoryEntity;
import com.spring.Final.modules.job_type.JobTypeEntity;
import com.spring.Final.modules.shared.enums.job_status.JobStatus;
import com.spring.Final.modules.shared.enums.job_status.JobStatusAttributeConverter;
import com.spring.Final.modules.skill.SkillEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Job")
@Table(name = "jobs")
public class JobEntity extends BaseEntity implements Serializable {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", referencedColumnName = "id")
    private EmployerEntity employer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_category_id", referencedColumnName = "id", nullable = false)
    private JobCategoryEntity jobCategory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_type_id", referencedColumnName = "id", nullable = false)
    private JobTypeEntity jobType;

//    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
//    private Set<JobProposalEntity> jobProposals;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "salary_from", columnDefinition = "decimal", scale = 2)
    private BigDecimal salaryFrom;

    @Column(name = "salary_to", columnDefinition = "decimal", scale = 2)
    private BigDecimal salaryTo;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "address_location", columnDefinition = "geometry")
    private Point addressLocation;

    @Column(name = "status", nullable = false)
    @Convert(converter = JobStatusAttributeConverter.class)
    private JobStatus status = JobStatus.OPEN;

    @Column(name = "expired_at", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date expiredAt;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = CommonHelper.getCurrentTime();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "job_skill",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<SkillEntity> skills;

    @JsonIgnore
    public EmployerEntity getEmployer() {
        return employer;
    }

    @JsonIgnore
    public JobCategoryEntity getJobCategory() {
        return jobCategory;
    }

    @JsonIgnore
    public JobTypeEntity getJobType() {
        return jobType;
    }

//    @JsonIgnore
//    public Set<JobProposalEntity> getJobProposals() {
//        return jobProposals;
//    }
}
