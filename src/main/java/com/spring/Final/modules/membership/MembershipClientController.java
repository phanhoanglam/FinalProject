package com.spring.Final.modules.membership;

import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.membership.projections.MembershipDetail;
import com.spring.Final.modules.shared.enums.membership_duration.MembershipDuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/memberships")
public class MembershipClientController extends ApiController {

    private final MembershipService service;

    public MembershipClientController(MembershipService service) {
        this.service = service;
    }

    @GetMapping("")
    public String list(Model modelView) {
        List<MembershipDetail> data = service.list();
        List<MembershipDetail> listMonthly = data.stream().filter(x->x.getDuration() == MembershipDuration.MONTHLY).collect(Collectors.toList());
        List<MembershipDetail> listYearly = data.stream().filter(x->x.getDuration() == MembershipDuration.YEARLY).collect(Collectors.toList());
        modelView.addAttribute("listMonthly", listMonthly);
        modelView.addAttribute("listYearly", listYearly);
        return "client/modules/memberships/membership";
    }
}
