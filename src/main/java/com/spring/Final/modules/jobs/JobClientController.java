package com.spring.Final.modules.jobs;

import com.spring.Final.core.common.General;
import com.spring.Final.core.exceptions.InvalidAddressException;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.job_proposal.projections.JobProposalList;
import com.spring.Final.modules.jobs.dtos.JobDTO;
import com.spring.Final.modules.jobs.dtos.SearchJobDTO;
import com.spring.Final.modules.jobs.projections.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        HomepageData data = service.list(page, size, dto);
        modelView.addAttribute("jobCategories", data.getJobCategories());
        modelView.addAttribute("jobTypes", data.getJobTypes());
        modelView.addAttribute("list", data.getList());
        modelView.addAttribute("skills", data.getSkills());
        modelView.addAttribute("searchJobDTO", dto);
        String url = General.ConvertURL(dto);
        modelView.addAttribute("url", url);

        return "client/modules/jobs/list";
    }

    @GetMapping("job-maps")
    public String listMaps(Model modelView,
                           Authentication authentication,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "12") int size,
                           SearchJobDTO dto
    ) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int id = (int) user.getInformation().get("id");
        HomepageData data = service.listMaps(page, size, dto, id);
        modelView.addAttribute("jobCategories", data.getJobCategories());
        modelView.addAttribute("jobTypes", data.getJobTypes());
        modelView.addAttribute("list", data.getList());
        modelView.addAttribute("skills", data.getSkills());
        modelView.addAttribute("searchJobDTO", dto);
        String url = General.ConvertURL(dto);
        modelView.addAttribute("url", url);

        return "client/modules/jobs/job-maps";
    }

    @GetMapping("/job-{slug}")
    public String getDetail(Authentication authentication, Model modelView, @PathVariable(value = "slug") String slug) {
        DetailData data = service.getDetail(slug, authentication);
        data.getJobDetails().setLocation(data.getJobDetails().getAddressLocation());
        modelView.addAttribute("detail", data.getJobDetails());
        modelView.addAttribute("proposal", data.getProposal());
        modelView.addAttribute("listSimilar", data.getJobLists());

        return "client/modules/jobs/detail";
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

        return "client/modules/jobs/post-job";
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

    @GetMapping("/dashboard/manage-jobs/{slug}")
    public String editJob(@PathVariable String slug, Model modelView) {
        int jobCategoryId = modelView.getAttribute("jobDTO") != null
                ? ((JobDTO) modelView.getAttribute("jobDTO")).getJobCategoryId()
                : 0;

        PostJobData data = this.service.getEditJobData(slug, jobCategoryId);
        modelView.addAttribute("jobCategories", data.getJobCategories());
        modelView.addAttribute("jobTypes", data.getJobTypes());
        modelView.addAttribute("skills", data.getSkills());
        modelView.addAttribute("status", data.getStatus());

        if (!modelView.containsAttribute("jobDTO")) {
            modelView.addAttribute("jobDTO", data.getJob());
        }
        if (!modelView.containsAttribute("addressMessage")) {
            modelView.addAttribute("addressMessage", "");
        }
        return "client/modules/jobs/edit-job";
    }

    @PostMapping("/dashboard/manage-jobs/{id}")
    public String editJobSubmit(
            @PathVariable int id,
            @Valid JobDTO dto,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        JobDetail jobDetail;

        try {
            dto.setId(id);
            jobDetail = service.updateOne(dto);
        } catch (InvalidAddressException e) {
            redirectAttributes.addFlashAttribute("addressMessage", "Invalid address");
            redirectAttributes.addFlashAttribute("jobDTO", dto);

            return "redirect:" + request.getHeader("Referer");
        }
        return "redirect:/dashboard/manage-jobs/" + jobDetail.getSlug();
    }


    @GetMapping("/dashboard/delete-job/{slug}")
    public String delete(@PathVariable String slug) {
        service.deleteOne(slug);

        return "redirect:/dashboard/manage-jobs";
    }

    @GetMapping("/dashboard/manage-jobs/{slug}/proposals")
    public String listProposals(@PathVariable(value = "slug") String slug, Model modelView) {
        ManageCandidatesData data = service.listProposals(slug);
        modelView.addAttribute("list", data.getList());
        modelView.addAttribute("job", data.getJob());

        return "client/modules/jobs/manage-candidates";
    }
}
