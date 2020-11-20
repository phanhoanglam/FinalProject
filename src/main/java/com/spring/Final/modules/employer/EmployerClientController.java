package com.spring.Final.modules.employer;

import com.spring.Final.core.exceptions.InvalidEmailOrPasswordException;
import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.employee.projections.EmployeeProfile;
import com.spring.Final.modules.employer.projections.EmployerDetail;
import com.spring.Final.modules.employer.projections.EmployerDetailData;
import com.spring.Final.modules.employer.projections.EmployerProfile;
import com.spring.Final.modules.jobs.projections.JobManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@CrossOrigin
public class EmployerClientController {
    @Autowired
    private EmployerService service;

    @GetMapping("/employers")
    public String list(Model modelView,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "9") int size) {
        modelView.addAttribute("list", this.service.list(page, size));

        return "client/modules/employers/list";
    }

    @GetMapping("/employers/{slug}")
    public String getDetail(Model modelView, @PathVariable String slug, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int userId = (int) user.getInformation().get("id");
        String role = (String) user.getInformation().get("role");

        if (role.equals("employer")) {
            userId = 0;
        }
        EmployerDetailData data = this.service.getDetail(slug, userId);

        modelView.addAttribute("detail", data.getEmployerDetail());
        modelView.addAttribute("jobList", data.getJobList());
        modelView.addAttribute("reviewList", data.getReviewList());
        modelView.addAttribute("allowReview", data.isAllowReview());

        return "client/modules/employers/detail";
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

    @GetMapping("/employer/profile")
    public String profile(Authentication authentication,
                          HttpServletResponse response,
                          Model modelView) throws IOException {
        if (authentication == null) {
            response.sendRedirect("/auth/login");
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        EmployerProfile profile = service.profile((int) user.getInformation().get("id"));
        modelView.addAttribute("profile", profile);

        return "client/modules/employers/profile";
    }

    @PostMapping("/employer/profile")
    public String editProfile(@Valid EmployerProfile dto,
                              Authentication authentication,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) throws IOException {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        dto.setId((int) user.getInformation().get("id"));
        boolean employerProfile = service.profileSubmit(dto);
        if(employerProfile == false){
            String errorMessage = "Wrong old password";
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/employer/profile";
    }
}
