package com.spring.Final.modules.employer;

import com.spring.Final.core.common.ApiUtils;
import com.spring.Final.core.common.JwtHelper;
import com.spring.Final.core.exceptions.UnauthorizedException;
import com.spring.Final.core.helpers.PermissionHelper;
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
        String token = ApiUtils.getRequestToken(request);

        if (token != null) {
            Map<String, String[]> validPaths = new HashMap<>();
            validPaths.put("/api/jobs", new String[]{"POST"});
            validPaths.put("/api/jobs/[0-9]+", new String[]{"PUT", "DELETE"});

            String subject = (String) JwtHelper.validateToken(token).get("subject");
            String path = request.getRequestURI();
            String method = request.getMethod();

            if (!PermissionHelper.compareAuthorizedUrls(validPaths, "employer", path, method, subject)) {
                throw new UnauthorizedException();
            }
        }

        return true;
    }
}
