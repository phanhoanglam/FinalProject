package com.spring.Final.modules.jobs;

import com.spring.Final.modules.jobs.dtos.SearchJobDTO;
import com.spring.Final.modules.jobs.projections.JobList;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/jobs")
public class JobClientController {

    private final JobService service;

    public JobClientController(JobService service) {
        this.service = service;
    }

    @GetMapping("")
    public String list(Model modelView,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "12") int size,
                             SearchJobDTO model
    ) {
        Page<JobList> data = service.list(page, size, model);
        if(data.getTotalPages() > 0){

        }
        modelView.addAttribute("listJobs", data);
        modelView.addAttribute("totalPage", data.getTotalPages());
        modelView.addAttribute("pageNumber", data.getPageable().getPageNumber());

        return "client/modules/jobs/job";
    }
}
