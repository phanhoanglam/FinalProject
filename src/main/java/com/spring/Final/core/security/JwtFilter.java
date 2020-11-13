package com.spring.Final.core.security;

import com.spring.Final.core.common.ApiUtils;
import com.spring.Final.core.common.JwtHelper;
import com.spring.Final.core.exceptions.BaseException;
import com.spring.Final.core.exceptions.InternalServerException;
import com.spring.Final.core.exceptions.ResourceNotFoundException;
import com.spring.Final.core.exceptions.UnauthorizedException;
import com.spring.Final.core.helpers.PermissionHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final RequestMappingHandlerMapping mapping;

    public JwtFilter(RequestMappingHandlerMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) {
        HandlerMethod handlerMethod;
        try {
            handlerMethod = getHandle(request);
            if (handlerMethod == null) {
                throw new ResourceNotFoundException();
            }
            String requestURI = request.getRequestURI();

            if (this.shouldIgnoreAuthorization(requestURI, request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = ApiUtils.getRequestToken(request);

            if (token == null || token.equals("")) {
                throw new UnauthorizedException();
            }
            JwtHelper.validateToken(token);
            filterChain.doFilter(request, response);
        } catch (BaseException e) {
            ApiUtils.writeException(e, response);
        } catch (Exception e) {
            ApiUtils.writeException(new InternalServerException(e.getMessage()), response);
        }
    }

    public HandlerMethod getHandle(HttpServletRequest request) throws Exception {
        HandlerExecutionChain handlerExeChain = mapping.getHandler(request);
        if (Objects.nonNull(handlerExeChain)) {
            return (HandlerMethod) handlerExeChain.getHandler();
        }
        return null;
    }

    /**
     * @Document https://www.baeldung.com/spring-exclude-filter
     */
    public Boolean shouldIgnoreAuthorization(String path, String method) {
        Map<String, String[]> validPaths = new HashMap<>();
        validPaths.put("/api/employees", new String[]{"GET"});
        validPaths.put("/api/employees/[a-z-]+", new String[]{"GET"});
        validPaths.put("/api/employees/login", new String[]{"POST"});
        validPaths.put("/api/employees/register", new String[]{"POST"});

        validPaths.put("/api/employers", new String[]{"GET"});
        validPaths.put("/api/employers/[a-z-]+", new String[]{"GET"});
        validPaths.put("/api/employers/login", new String[]{"POST"});
        validPaths.put("/api/employers/register", new String[]{"POST"});

        validPaths.put("/api/jobs", new String[]{"GET"});
        validPaths.put("/api/skills", new String[]{"GET"});
        validPaths.put("/api/job-categories", new String[]{"GET"});
        validPaths.put("/api/job-types", new String[]{"GET"});
        validPaths.put("/api/payments/webhook", new String[]{"POST"});

        validPaths.put("/api/medias/[a-zA-Z0-9-]+", new String[]{"GET"});
        validPaths.put("/api/jobs/[a-zA-Z0-9-]+", new String[]{"GET"});

        if (PermissionHelper.compareUrls(validPaths, path, method)) {
            return true;
        }
        return false;
    }
}
