package com.spring.Final.modules.job_proposal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.modules.employee.EmployeeEntity;
import com.spring.Final.modules.jobs.JobEntity;
import com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus;
import com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatusAttributeConverter;
import com.spring.Final.modules.shared.enums.job_proposal_type.JobProposalType;
import com.spring.Final.modules.shared.enums.job_proposal_type.JobProposalTypeAttributeConverter;
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
@Entity(name = "JobProposal")
@Table(name = "job_proposals")
public class JobProposalEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", referencedColumnName = "id", nullable = false)
    private JobEntity job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private EmployeeEntity employee;

    @Column(name = "type", nullable = false)
    @Convert(converter = JobProposalTypeAttributeConverter.class)
    private JobProposalType type = JobProposalType.PROPOSAL;

    @Column(name = "status", nullable = false)
    @Convert(converter = JobProposalStatusAttributeConverter.class)
    private JobProposalStatus status = JobProposalStatus.PENDING;

    @Column(name = "started_at")
    @Temporal(TemporalType.DATE)
    private Date startedAt;

    @Column(name = "ended_at")
    @Temporal(TemporalType.DATE)
    private Date endedAt;

//    @Column(name = "description")
//    private String description;

    @Column(name = "attachments", columnDefinition = "TEXT")
    private String attachments;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

    @JsonIgnore
    public JobEntity getJob() {
        return job;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }
}
