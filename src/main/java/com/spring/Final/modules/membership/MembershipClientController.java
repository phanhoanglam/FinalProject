package com.spring.Final.modules.membership;

import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.membership.projections.MembershipDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RequestMapping("/memberships")
public class MembershipClientController extends ApiController {

    private final MembershipService service;

    public MembershipClientController(MembershipService service) {
        this.service = service;
    }

    @GetMapping("")
    public String list(Model modelView) {
//        List<MembershipDetail> data = service.list();

        return "client/modules/memberships/membership";
    }
}
