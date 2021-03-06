package com.spring.Final.modules.membership;

import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.employer.EmployerService;
import com.spring.Final.modules.employer.projections.EmployerMembership;
import com.spring.Final.modules.membership.projections.MembershipData;
import com.spring.Final.modules.membership.projections.MembershipDetail;
import com.spring.Final.modules.shared.enums.membership_duration.MembershipDuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipService extends ApiService<MembershipEntity, MembershipRepository> {
    public static final float VAT_RATE = 10;

    @Autowired
    private EmployerService employerService;

    public MembershipService(MembershipRepository repository) {
        this.repository = repository;
    }

    public MembershipData list(int employerId) {
        EmployerMembership membership = employerService.getMembership(employerId);

        List<MembershipEntity> memberships = this.repository.findAll(Sort.by(Sort.Direction.ASC, "price"));
        List<MembershipDetail> list = memberships.stream()
                .map(e -> this.modelMapper.map(e, MembershipDetail.class))
                .collect(Collectors.toList());

        List<MembershipDetail> listMonthly = list.stream()
                .filter(x -> x.getDuration() == MembershipDuration.MONTHLY)
                .collect(Collectors.toList());
        List<MembershipDetail> listYearly = list.stream()
                .filter(x -> x.getDuration() == MembershipDuration.YEARLY)
                .collect(Collectors.toList());

        return new MembershipData(
                listMonthly,
                listYearly,
                MembershipService.VAT_RATE,
                membership.getMembership() != null && membership.getMembershipEndAt().after(CommonHelper.getCurrentTime())
        );
    }
}
