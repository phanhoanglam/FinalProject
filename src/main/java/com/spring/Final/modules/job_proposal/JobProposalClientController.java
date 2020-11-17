package com.spring.Final.modules.job_proposal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
@RequestMapping("dashboard/job-proposals")
public class JobProposalClientController {
    @Autowired
    private JobProposalService service;

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
