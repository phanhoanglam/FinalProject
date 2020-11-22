package com.spring.Final.modules.job_proposal;

import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.job_proposal.projections.JobProposalEmployee;
import com.spring.Final.modules.shared.data.Countries;
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
@RequestMapping("/dashboard")
public class JobProposalClientController {
    @Autowired
    private JobProposalService service;

    @GetMapping("/manage-proposals")
    public String listByEmployee(Model modelView,
                                 Authentication authentication,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "8") int size
    ) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int id = (int) user.getInformation().get("id");

        Page<JobProposalEmployee> list = service.listJobByEmployee(page, size, id);
        modelView.addAttribute("list", list);
        modelView.addAttribute("countries", Countries.getCountries());

        return "client/modules/jobs/manage-proposals";
    }

    @GetMapping("/job-proposals/accept/{id}")
    public String accept(
            @PathVariable(value = "id") int id,
            HttpServletRequest request
    ) {
        this.service.accept(id);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/job-proposals/reject/{id}")
    public String reject(
            @PathVariable(value = "id") int id,
            HttpServletRequest request
    ) {
        this.service.reject(id);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/job-proposals/fail/{id}")
    public String setFailed(
            @PathVariable(value = "id") int id,
            HttpServletRequest request
    ) {
        this.service.setFailed(id);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/job-proposals/succeed/{id}")
    public String setSucceeded(
            @PathVariable(value = "id") int id,
            HttpServletRequest request
    ) {
        this.service.setSucceeded(id);

        return "redirect:" + request.getHeader("Referer");
    }
}
