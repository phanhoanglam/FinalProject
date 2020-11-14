package com.spring.Final.modules.job_type;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
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
@Entity(name = "JobType")
@Table(name = "job_types")
public class JobTypeEntity extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = CommonHelper.getCurrentTime();

    public JobTypeEntity(String name) {
        this.name = name;
        this.createdAt = createdAt;
    }
}
