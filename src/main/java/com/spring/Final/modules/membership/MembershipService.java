package com.spring.Final.modules.membership;

import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.membership.projections.MembershipDetail;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipService extends ApiService<MembershipEntity, MembershipRepository> {
    public MembershipService(MembershipRepository repository) {
        this.repository = repository;
    }

    public List<MembershipDetail> list() {
        List<MembershipEntity> memberships = this.repository.findAll(Sort.by(Sort.Direction.ASC, "price"));

        return memberships.stream()
                .map(e -> this.modelMapper.map(e, MembershipDetail.class))
                .collect(Collectors.toList());
    }
}
