package com.spring.Final.modules.job_skill;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.modules.jobs.JobEntity;
import com.spring.Final.modules.skill.SkillEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "JobSkill")
@Table(name = "job_skill")
public class JobSkillEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

    @ManyToOne()
    @JoinColumn(name = "job_id", referencedColumnName = "id", nullable = false)
    private JobEntity job;

    @ManyToOne()
    @JoinColumn(name = "skill_id", referencedColumnName = "id", nullable = false)
    private SkillEntity skill;

}
