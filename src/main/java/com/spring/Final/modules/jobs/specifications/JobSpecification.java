package com.spring.Final.modules.jobs.specifications;

import com.spring.Final.modules.jobs.JobEntity;
import com.spring.Final.modules.shared.dtos.SearchDTO;
import com.spring.Final.modules.shared.enums.job_status.JobStatus;
import com.spring.Final.modules.shared.specifications.EntitySpecification;
import com.spring.Final.modules.skill.SkillEntity;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;

public class JobSpecification extends EntitySpecification<JobEntity> {
    public JobSpecification(SearchDTO dto) {
        super(dto);
    }

    @Override
    public Predicate toPredicate(Root<JobEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.disjunction();
        ArrayList<Predicate> andPredicates = new ArrayList<>();
        andPredicates.add(cb.equal(root.get("status"), JobStatus.OPEN));

        if (dto.getNames().length != 0) {
            int index = 0;
            Predicate[] predicates = new Predicate[dto.getNames().length];

            for (String name : dto.getNames()) {
                predicates[index] = cb.like(root.get("name"), "%" + name + "%");
                index++;
            }
            andPredicates.add(cb.or(predicates));
        }
        if (dto.getJobCategories().size() != 0) {
            andPredicates.add(
                    root.get("jobCategory").in(dto.getJobCategories())
            );
        }
        if (dto.getJobTypes().size() != 0) {
            andPredicates.add(
                    root.get("jobType").in(dto.getJobTypes())
            );
        }
        if (dto.getSkills().size() != 0) {
            Subquery<SkillEntity> skillSubQuery = query.subquery(SkillEntity.class);
            Root<SkillEntity> skill = skillSubQuery.from(SkillEntity.class);
            Expression<Collection<JobEntity>> skillJobs = skill.get("jobs");

            skillSubQuery.select(skill);
            skillSubQuery.where(skill.get("id").in(dto.getSkills()), cb.isMember(root, skillJobs));

            andPredicates.add(cb.exists(skillSubQuery));
        }
        if (dto.getSalaryFrom() != 0) {
            andPredicates.add(
                    cb.greaterThanOrEqualTo(root.get("salaryFrom"), dto.getSalaryFrom())
            );
        }
        if (dto.getSalaryTo() != 0) {
            andPredicates.add(
                    cb.lessThanOrEqualTo(root.get("salaryTo"), dto.getSalaryTo())
            );
        }
        andPredicates = this.getSearchPredicates(andPredicates, root, cb);
        Predicate[] predicates = new Predicate[andPredicates.size()];
        predicates = andPredicates.toArray(predicates);
        predicate.getExpressions().add(cb.and(predicates));

        return predicate;
    }
}
