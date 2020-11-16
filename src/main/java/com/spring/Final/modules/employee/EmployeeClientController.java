package com.spring.Final.modules.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/employees")
public class EmployeeClientController {

    @Autowired
    private EmployeeService employeeService;

    public EmployeeClientController(EmployeeService employeeService) {
        this.employeeService = employeeService;
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
