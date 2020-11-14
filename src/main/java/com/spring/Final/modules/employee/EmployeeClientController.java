package com.spring.Final.modules.employee;

import com.spring.Final.modules.employee.dtos.LoginDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/employees")
public class EmployeeClientController {

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model, Authentication authentication) throws IOException {
        if (authentication != null && authentication.isAuthenticated()) {
//            Get authentication information
//            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
//            user.getInformation();
            response.sendRedirect("/");
        }
        String errorMessage = null;
		HttpSession session = request.getSession(false);

        if (session != null) {
            AuthenticationException e = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (e != null) {
                errorMessage = "Invalid email or password";
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
        }
        model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("loginDTO", new LoginDTO());

		return "client/modules/employees/login";
	}
}
