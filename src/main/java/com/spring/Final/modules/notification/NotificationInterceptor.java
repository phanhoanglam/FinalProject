package com.spring.Final.modules.notification;

import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.jobs.JobService;
import com.spring.Final.modules.jobs.projections.JobDetail;
import com.spring.Final.modules.notification.projections.Notification;
import com.spring.Final.modules.shared.enums.notification_reference_type.ReferenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationInterceptor implements HandlerInterceptor {
    @Autowired
    JobService jobService;

    @Autowired
    NotificationService notificationService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelView) {
        if (modelView == null) return;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            int userId = (int) userDetails.getInformation().get("id");
            String role = (String) userDetails.getInformation().get("role");

            modelView.addObject("notifications", this.notificationService.findAllByUser(userId, role));
        } else {
            modelView.addObject("notifications", new ArrayList<Notification>());
        }
    }
}
