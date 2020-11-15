package com.spring.Final.modules.jobs;

import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.jobs.dtos.JobDTO;
import com.spring.Final.modules.jobs.dtos.SearchJobDTO;
import com.spring.Final.modules.jobs.projections.DetailData;
import com.spring.Final.modules.jobs.projections.HomepageData;
import com.spring.Final.modules.jobs.projections.JobList;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import com.spring.Final.modules.jobs.projections.PostJobData;
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

    @GetMapping("/{slug}")
    public String getDetail(Authentication authentication , HttpServletResponse response, Model modelView, @PathVariable(value = "slug") String slug) throws IOException {
        if (authentication == null) {
            response.sendRedirect("/auth/login");
        }
        DetailData data = service.getDetail(slug, authentication);
        data.getJobDetails().setLocation(data.getJobDetails().getAddressLocation());
        modelView.addAttribute("detail", data.getJobDetails());
        modelView.addAttribute("proposal", data.getProposal());
        modelView.addAttribute("listSimilar", data.getJobLists());
        return "client/modules/job-details/job-detail";
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
