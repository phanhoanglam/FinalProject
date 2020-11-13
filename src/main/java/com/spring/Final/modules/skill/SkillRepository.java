package com.spring.Final.modules.skill;

import com.spring.Final.modules.shared.dtos.NameOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<SkillEntity, Integer> {
    @Query(value = "select f from Skill f where f.jobCategory.id in ?1")
    Page<NameOnly> findAllAsNameOnly(Pageable var1, int[] jobCategories);
}
