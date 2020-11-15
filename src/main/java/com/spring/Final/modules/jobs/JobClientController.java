package com.spring.Final.modules.jobs;

import com.spring.Final.core.exceptions.InvalidAddressException;
import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.jobs.dtos.JobDTO;
import com.spring.Final.modules.jobs.dtos.SearchJobDTO;
import com.spring.Final.modules.jobs.projections.HomepageData;
import com.spring.Final.modules.jobs.projections.JobList;
import com.spring.Final.modules.jobs.projections.PostJobData;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        if (!modelView.containsAttribute("jobDTO")) {
            modelView.addAttribute("jobDTO", new JobDTO());
        }
        if (!modelView.containsAttribute("addressMessage")) {
            modelView.addAttribute("addressMessage", "");
        }
        PostJobData data = this.service.getPostJobData(((JobDTO) modelView.getAttribute("jobDTO")).getJobCategoryId());
        modelView.addAttribute("jobCategories", data.getJobCategories());
        modelView.addAttribute("jobTypes", data.getJobTypes());
        modelView.addAttribute("skills", data.getSkills());

        return "client/modules/employers/post-job";
    }

    @PostMapping("/dashboard/post-job")
    public String postJobSubmit(@Valid JobDTO dto, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        dto.setEmployerId((int) user.getInformation().get("id"));

        try {
            service.createOne(dto);
        } catch (InvalidAddressException e) {
            redirectAttributes.addFlashAttribute("addressMessage", "Invalid address");
            redirectAttributes.addFlashAttribute("jobDTO", dto);

            return "redirect:/dashboard/post-job";
        }

        return "redirect:/dashboard/manage-jobs";
    }
}
