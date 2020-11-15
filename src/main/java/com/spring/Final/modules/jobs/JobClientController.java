package com.spring.Final.modules.jobs;

import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.jobs.dtos.JobDTO;
import com.spring.Final.modules.jobs.dtos.SearchJobDTO;
import com.spring.Final.modules.jobs.projections.HomepageData;
import com.spring.Final.modules.jobs.projections.JobList;
import com.spring.Final.modules.jobs.projections.PostJobData;
import com.spring.Final.modules.shared.enums.job_status.JobStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

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

    @GetMapping("/dashboard/post-job")
    public String postJob(Model modelView) {
        PostJobData data = this.service.getPostJobData();
        modelView.addAttribute("jobCategories", data.getJobCategories());
        modelView.addAttribute("jobTypes", data.getJobTypes());

        if (!modelView.containsAttribute("jobDTO")) {
            modelView.addAttribute("jobDTO", new JobDTO());
        }

        return "client/modules/employers/post-job";
    }

    @PostMapping("/dashboard/post-job")
    public String postJobSubmit(@Valid JobDTO model, Authentication authentication) throws IOException {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        model.setEmployerId((int) user.getInformation().get("id"));

        service.createOne(model);

        return "redirect:/dashboard/manage-jobs";
    }
}
