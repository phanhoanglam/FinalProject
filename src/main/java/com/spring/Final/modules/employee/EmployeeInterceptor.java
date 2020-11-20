package com.spring.Final.modules.employee;

import com.spring.Final.core.exceptions.UnauthorizedException;
import com.spring.Final.core.helpers.PermissionHelper;
import com.spring.Final.modules.auth.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmployeeInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication.getPrincipal() instanceof String)) {
            Map<String, String[]> validPaths = new HashMap<>();
            validPaths.put("/api/job-proposals", new String[]{"POST"});
            validPaths.put("/api/job-proposals/resume", new String[]{"POST"});
            validPaths.put("/api/reviews/employers", new String[]{"POST"});

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            String subject = (String) user.getInformation().get("role");
            String path = request.getRequestURI();
            String method = request.getMethod();

            if (!PermissionHelper.compareAuthorizedUrls(validPaths, "employee", path, method, subject)) {
                throw new UnauthorizedException();
            }
        }

        return true;
    }
}
