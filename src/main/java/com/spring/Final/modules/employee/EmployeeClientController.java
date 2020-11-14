package com.spring.Final.modules.employee;

import com.spring.Final.modules.employee.dtos.LoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employees")
public class EmployeeClientController {
    @GetMapping("/login")
    public String login(Model model) {
		model.addAttribute("loginDTO", new LoginDTO());

		return "client/modules/employees/login";
	}
}
