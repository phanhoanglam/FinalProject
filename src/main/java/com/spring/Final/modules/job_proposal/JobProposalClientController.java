package com.spring.Final.modules.job_proposal;

import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.job_proposal.projections.JobProposalEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("dashboard/job-proposals")
public class JobProposalClientController {
    @Autowired
    private JobProposalService service;

    @GetMapping("/manage-proposals")
    public String listByEmployee(Model modelView,
                                 Authentication authentication,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "12") int size
    ) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int id = (int) user.getInformation().get("id");
        Page<JobProposalEmployee> list = service.listJobByEmployee(page, size, id);
        modelView.addAttribute("list", list);
        return "client/modules/jobs/manage-proposals";
    }

    @GetMapping("/accept/{id}")
    public String accept(
            @PathVariable(value = "id") int id,
            HttpServletRequest request
    ) {
        this.service.accept(id);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/reject/{id}")
    public String reject(
            @PathVariable(value = "id") int id,
            HttpServletRequest request
    ) {
        this.service.reject(id);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/fail/{id}")
    public String setFailed(
            @PathVariable(value = "id") int id,
            HttpServletRequest request
    ) {
        this.service.setFailed(id);

        return "redirect:" + request.getHeader("Referer");
    }
}
