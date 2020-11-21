package com.spring.Final.modules.page;

import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.page.projections.DashboardData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageClientController {
    @Autowired
    private PageService pageService;

    @GetMapping("/dashboard")
    public String renderDashboard(Model modelView, Authentication authentication) {
        // count something here
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String role = (String) userDetails.getInformation().get("role");
        int userId = (int) userDetails.getInformation().get("id");

        DashboardData dashboardData = this.pageService.getDashboardData(userId, role);
        modelView.addAttribute("detail", dashboardData);
        modelView.addAttribute("role", role);

        return "client/modules/pages/dashboard";
    }

    @GetMapping("/contact")
    public String contact() {
        return "client/modules/pages/contact";
    }
}
