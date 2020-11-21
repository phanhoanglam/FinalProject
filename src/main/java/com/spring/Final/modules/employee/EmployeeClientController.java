package com.spring.Final.modules.employee;

import com.spring.Final.core.common.General;
import com.spring.Final.core.exceptions.ResourceNotFoundException;
import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.employee.projections.EmployeeProfile;
import com.spring.Final.modules.employee.projections.ProfilePageData;
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
import javax.validation.Valid;
import java.io.IOException;


@Controller
@RequestMapping("/")
public class EmployeeClientController {

    @Autowired
    private EmployeeService employeeService;

    public EmployeeClientController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
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

    @GetMapping("/employees/{slug}")
    public String getDetail(@PathVariable String slug, Model modelView) {
        EmployeeDetailData data = employeeService.getDetail(slug);

        modelView.addAttribute("detail", data.getEmployeeDetail());
        modelView.addAttribute("history", data.getEmploymentHistory());
        modelView.addAttribute("reviews", data.getReviewList());

        return "client/modules/employees/detail";
    }

    @GetMapping("/dashboard/employee/profile")
    public String profile(Authentication authentication, Model modelView) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int id = (int) user.getInformation().get("id");

        ProfilePageData data = employeeService.getProfile(id);
        modelView.addAttribute("skills", data.getSkills());
        modelView.addAttribute("jobCategories", data.getJobCategories());

        if (!modelView.containsAttribute("detail")) {
            modelView.addAttribute("detail", data.getEmployeeProfile());
        }

        return "client/modules/employees/profile";
    }

    @PostMapping("/dashboard/employee/profile")
    public String editProfile(@Valid EmployeeProfile dto, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        dto.setId((int) user.getInformation().get("id"));

        EmployeeEntity result = employeeService.submitProfile(dto);

        if (result == null) {
            String errorMessage = "Wrong old password";
            redirectAttributes.addFlashAttribute("detail", dto);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/dashboard/employee/profile";
    }
}
