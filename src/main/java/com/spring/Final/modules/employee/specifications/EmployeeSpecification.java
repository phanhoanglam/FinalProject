package com.spring.Final.modules.employee.specifications;

import com.spring.Final.modules.employee.EmployeeEntity;
import com.spring.Final.modules.job_category.JobCategoryEntity;
import com.spring.Final.modules.shared.dtos.SearchDTO;
import com.spring.Final.modules.shared.specifications.EntitySpecification;
import com.spring.Final.modules.skill.SkillEntity;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;

public class EmployeeSpecification extends EntitySpecification<EmployeeEntity> {
    public EmployeeSpecification(SearchDTO dto) {
        super(dto);
    }

    @Override
    public Predicate toPredicate(Root<EmployeeEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.disjunction();
        ArrayList<Predicate> andPredicates = new ArrayList<>();

        if (dto.getJobCategories().size() != 0) {
            Subquery<JobCategoryEntity> jcSubQuery = query.subquery(JobCategoryEntity.class);
            Root<JobCategoryEntity> jc = jcSubQuery.from(JobCategoryEntity.class);
            Expression<Collection<EmployeeEntity>> jcEmployees = jc.get("employees");

            jcSubQuery.select(jc);
            jcSubQuery.where(jc.get("id").in(dto.getJobCategories()), cb.isMember(root, jcEmployees));

            andPredicates.add(cb.exists(jcSubQuery));
        }
        if (dto.getSkills().size() != 0) {
            Subquery<SkillEntity> skillSubQuery = query.subquery(SkillEntity.class);
            Root<SkillEntity> jc = skillSubQuery.from(SkillEntity.class);
            Expression<Collection<EmployeeEntity>> skillEmployees = jc.get("employees");

            skillSubQuery.select(jc);
            skillSubQuery.where(jc.get("id").in(dto.getSkills()), cb.isMember(root, skillEmployees));

            andPredicates.add(cb.exists(skillSubQuery));
        }
        if (dto.getNames().length != 0) {
            int index = 0;
            Predicate[] predicates = new Predicate[dto.getNames().length * 2];

            for (String name : dto.getNames()) {
                predicates[index] = cb.like(root.get("firstName"), "%" + name + "%");
                predicates[index + 1] = cb.like(root.get("lastName"), "%" + name + "%");
                index += 2;
            }
            andPredicates.add(cb.or(predicates));
        }
        if (dto.getSalaryFrom() != 0) {
            andPredicates.add(
                    cb.greaterThanOrEqualTo(root.get("minHourlyRate"), dto.getSalaryFrom())
            );
        }
        if (dto.getSalaryTo() != 0) {
            andPredicates.add(
                    cb.lessThanOrEqualTo(root.get("minHourlyRate"), dto.getSalaryTo())
            );
        }
        andPredicates = this.getSearchPredicates(andPredicates, root, cb);
        Predicate[] predicates = new Predicate[andPredicates.size()];
        predicates = andPredicates.toArray(predicates);
        predicate.getExpressions().add(cb.and(predicates));

        return predicate;
    }
}
