package com.spring.Final.modules.employee;

import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.employee.projections.EmployeeProfile;
import com.spring.Final.modules.employee.projections.ProfilePageData;
import com.spring.Final.modules.jobs.dtos.JobDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;


@Controller
@RequestMapping("/employees")
public class EmployeeClientController {

    @Autowired
    private EmployeeService employeeService;

    public EmployeeClientController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication,
                          HttpServletResponse response,
                          Model modelView) throws IOException {
        if (authentication == null) {
            response.sendRedirect("/auth/login");
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int id = (int) user.getInformation().get("id");

        ProfilePageData data = employeeService.profile(id);
        modelView.addAttribute("detail", data.getEmployeeProfile());
        modelView.addAttribute("skills", data.getSkills());
        return "client/modules/employees/profile";
    }

    @PostMapping("/profile")
    public String editProfile(@Valid EmployeeProfile dto,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) throws IOException {
        return "redirect:/employees/profile";
    }
}
