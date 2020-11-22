package com.spring.Final.modules.membership;

import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.membership.projections.MembershipData;
import com.spring.Final.modules.membership.projections.MembershipDetail;
import com.spring.Final.modules.shared.enums.membership_duration.MembershipDuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public String list(Model modelView, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int employerId = (int) user.getInformation().get("id");

        MembershipData data = service.list(employerId);

        modelView.addAttribute("listMonthly", data.getListMonthly());
        modelView.addAttribute("listYearly", data.getListYearly());
        modelView.addAttribute("VATRate", data.getVATRate());
        modelView.addAttribute("alreadyRegistered", data.isAlreadyRegistered());

        return "client/modules/memberships/membership";
    }
}
