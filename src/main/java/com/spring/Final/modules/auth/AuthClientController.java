package com.spring.Final.modules.auth;

import com.spring.Final.core.exceptions.EntityExistException;
import com.spring.Final.core.exceptions.InvalidAddressException;
import com.spring.Final.modules.auth.dtos.LoginDTO;
import com.spring.Final.modules.auth.dtos.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/auth")
public class AuthClientController {
    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model, Authentication authentication) throws IOException {
        if (authentication != null && authentication.isAuthenticated()) {
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

        return "client/modules/auth/login";
    }

    @GetMapping("/register")
    public String register(HttpServletResponse response, Model model, Authentication authentication) throws IOException {
        if (authentication != null && authentication.isAuthenticated()) {
            response.sendRedirect("/");
        }
        if (!model.containsAttribute("message")) {
            model.addAttribute("message", null);
        }
        if (!model.containsAttribute("accountType")) {
            model.addAttribute("accountType", "employee");
        }
        if (!model.containsAttribute("registerDTO")) {
            model.addAttribute("registerDTO", new RegisterDTO());
        }
        return "client/modules/auth/register";
    }

    @PostMapping("/register")
    public String registerSubmit(
            @RequestParam String accountType,
            @Valid RegisterDTO dto,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }
        try {
            accountType = accountType.split(",")[0];
            this.authService.register(dto, accountType);
        } catch (EntityExistException | IOException | InvalidAddressException e) {
            String message;

            if (e instanceof InvalidAddressException) {
                message = e.getMessage();
            } else if (e instanceof EntityExistException) {
                message = "Email is already used";
            } else {
                message = "Register failed";
            }
            redirectAttributes.addFlashAttribute("message", message);
            redirectAttributes.addFlashAttribute("accountType", accountType);
            redirectAttributes.addFlashAttribute("registerDTO", dto);

            return "redirect:/auth/register";
        }
        return "redirect:/auth/login";
    }
}
