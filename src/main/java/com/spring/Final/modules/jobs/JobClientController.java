package com.spring.Final.modules.jobs;

import com.spring.Final.modules.jobs.dtos.SearchJobDTO;
import com.spring.Final.modules.jobs.projections.HomepageData;
import com.spring.Final.modules.jobs.projections.JobList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class JobClientController {

    private final JobService service;

    public JobClientController(JobService service) {
        this.service = service;
    }

    @GetMapping("")
    public String list(Model modelView,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "12") int size,
                       SearchJobDTO dto
    ) {
        HomepageData<JobList> data = service.list(page, size, dto);
        modelView.addAttribute("jobCategories", data.getJobCategories());
        modelView.addAttribute("jobTypes", data.getJobTypes());
        modelView.addAttribute("list", data.getList());
        modelView.addAttribute("skills", data.getSkills());
        modelView.addAttribute("searchJobDTO", dto);

        return "client/modules/jobs/list";
    }
}
