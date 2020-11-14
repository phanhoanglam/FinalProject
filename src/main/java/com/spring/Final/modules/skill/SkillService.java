package com.spring.Final.modules.skill;

import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.shared.dtos.NameOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SkillService extends ApiService<SkillEntity, SkillRepository> {
    public SkillService(SkillRepository repository) {
        this.repository = repository;
    }

    public Page<NameOnly> findAllAsNameOnly(int pageNumber, int size, int[] jobCategories) {
        Pageable page = PageRequest.of(this.getPage(pageNumber), size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (jobCategories.length == 0) {
            jobCategories = new int[]{0};
        }

        return this.repository.findAllAsNameOnly(page, jobCategories);
    }
}
