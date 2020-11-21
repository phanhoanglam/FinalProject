package com.spring.Final.modules.employer;

import com.spring.Final.core.common.JwtHelper;
import com.spring.Final.core.exceptions.UnauthorizedException;
import com.spring.Final.core.helpers.PermissionHelper;
import com.spring.Final.modules.auth.CustomUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmployerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Map<String, String[]> validPaths = new HashMap<>();
            validPaths.put("/dashboard/manage-jobs", new String[]{"GET"});
            validPaths.put("/dashboard/post-job", new String[]{"GET", "POST"});
            validPaths.put("/dashboard/manage-jobs/[0-9a-zA-Z-]+", new String[]{"GET", "POST", "DELETE"});
            validPaths.put("/dashboard/manage-jobs/[0-9a-zA-Z-]+/proposals", new String[]{"GET"});
            validPaths.put("/dashboard/job-proposals/accept/[0-9]+", new String[]{"GET"});
            validPaths.put("/dashboard/job-proposals/reject/[0-9]+", new String[]{"GET"});
            validPaths.put("/dashboard/job-proposals/fail/[0-9]+", new String[]{"GET"});
            validPaths.put("/api/reviews/employers", new String[]{"POST"});
            validPaths.put("/dashboard/employer/profile", new String[]{"GET", "POST"});

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            String subject = (String) user.getInformation().get("role");
            String path = request.getRequestURI();
            String method = request.getMethod();

            if (!PermissionHelper.compareAuthorizedUrls(validPaths, "employer", path, method, subject)) {
                throw new UnauthorizedException();
            }
        }

        return true;
    }
}
