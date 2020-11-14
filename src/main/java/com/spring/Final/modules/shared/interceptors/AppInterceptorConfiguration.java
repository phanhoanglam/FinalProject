package com.spring.Final.modules.shared.interceptors;

import com.spring.Final.modules.employee.EmployeeInterceptor;
import com.spring.Final.modules.employer.EmployerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class AppInterceptorConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    EmployeeInterceptor employeeInterceptor;

    @Autowired
    EmployerInterceptor employerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(employeeInterceptor);
        registry.addInterceptor(employerInterceptor);
    }
}
