package com.spring.Final.modules.employer;

import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.jobs.projections.JobManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin
//@RequestMapping("/")
public class EmployerClientController {
    @Autowired
    private EmployerService service;

    @GetMapping("/employers")
    public String list() {
        return "client/modules/employers/list";
    }

    @GetMapping("/dashboard/manage-jobs")
    public String listJobs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            Authentication authentication,
            Model model
    ) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int id = (int) user.getInformation().get("id");

        Page<JobManage> list = this.service.listJobs(page, size, id);
        model.addAttribute("list", list);

        return "client/modules/jobs/manage-jobs";
    }
}
