package com.spring.Final.modules.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageClientController {
    @GetMapping("/dashboard")
    public String renderDashboard() {
        return "client/modules/pages/dashboard";
    }

    @GetMapping("/contact")
    public String contact() {
        return "client/modules/pages/contact";
    }
}
