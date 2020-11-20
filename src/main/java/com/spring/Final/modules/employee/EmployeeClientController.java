package com.spring.Final.modules.employee;

import com.spring.Final.core.common.General;
import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.employee.projections.EmployeeProfile;
import com.spring.Final.modules.employee.projections.ProfilePageData;
import com.spring.Final.modules.jobs.dtos.JobDTO;
import com.spring.Final.modules.employee.dtos.SearchEmployeeDTO;
import com.spring.Final.modules.employee.projections.EmployeeDetailData;
import com.spring.Final.modules.employee.projections.ListEmployeesData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size,
            SearchEmployeeDTO dto,
            Model modelView
    ) {
        ListEmployeesData data = employeeService.list(page, size, dto);

        modelView.addAttribute("jobCategories", data.getJobCategories());
        modelView.addAttribute("list", data.getList());
        modelView.addAttribute("skills", data.getSkills());
        modelView.addAttribute("searchJobDTO", dto);
        String url = General.ConvertURL(dto);
        modelView.addAttribute("url", url);

        return "client/modules/employees/list";
    }

    @GetMapping("/{slug}")
    public String getDetail(@PathVariable String slug, Model modelView) {
        EmployeeDetailData data = employeeService.getDetail(slug);

        modelView.addAttribute("detail", data.getEmployeeDetail());
        modelView.addAttribute("history", data.getEmploymentHistory());
        modelView.addAttribute("reviews", data.getReviewList());

        return "client/modules/employees/detail";
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
                              Authentication authentication,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) throws IOException {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        dto.setId((int) user.getInformation().get("id"));
        EmployeeProfile profile = employeeService.profileSubmit(dto);
        return "redirect:/employees/profile";
    }
}
