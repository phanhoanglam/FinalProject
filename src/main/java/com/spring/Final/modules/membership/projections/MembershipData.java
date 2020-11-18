package com.spring.Final.modules.membership.projections;

import lombok.Data;

import java.util.List;

@Data
public class MembershipData {
    private List<MembershipDetail> listMonthly;

    private List<MembershipDetail> listYearly;

    private float VATRate;

    public MembershipData(List<MembershipDetail> listMonthly, List<MembershipDetail> listYearly, float VATRate) {
        this.listMonthly = listMonthly;
        this.listYearly = listYearly;
        this.VATRate = VATRate;
    }
}
