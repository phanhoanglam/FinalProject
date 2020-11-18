package com.spring.Final.modules.employee;

import com.spring.Final.modules.employee.dtos.SearchEmployeeDTO;
import com.spring.Final.modules.employee.projections.ListEmployeesData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;


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
            @RequestParam(defaultValue = "9") int size,
            SearchEmployeeDTO dto,
            Model modelView
    ) {
        ListEmployeesData data = employeeService.list(page, size, dto);

        modelView.addAttribute("jobCategories", data.getJobCategories());
        modelView.addAttribute("list", data.getList());
        modelView.addAttribute("skills", data.getSkills());
        modelView.addAttribute("searchJobDTO", dto);

        return "client/modules/employees/list";
    }

    @GetMapping("/profile/{slug}")
    public String profile(Authentication authentication,
                          HttpServletResponse response,
                          Model modelView,
                          @PathVariable(value = "slug") String slug)
    {

        return "client/modules/employees/profile";
    }
}
