package com.spring.Final.modules.auth;

import com.spring.Final.modules.employee.EmployeeEntity;
import com.spring.Final.modules.employee.EmployeeRepository;
import com.spring.Final.modules.employer.EmployerEntity;
import com.spring.Final.modules.employer.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String accountType = (((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()).getParameter("accountType");

        boolean isEmployee = accountType.equals("employee");
        EmployeeEntity employee = null;
        EmployerEntity employer = null;

        if (isEmployee) {
            employee = employeeRepository.findByEmail(email);
        } else {
            employer = employerRepository.findByEmail(email);
        }

        if (isEmployee) {
            if (employee != null) {
                HashMap<String, Object> information = new HashMap<>();
                information.put("id", employee.getId());
                information.put("role", "employee");

                return new CustomUserDetails(employee.getEmail(), employee.getPassword(), information, new ArrayList<>());
            }
            throw new UsernameNotFoundException(email);
        }
        if (employer != null) {
            HashMap<String, Object> information = new HashMap<>();
            information.put("id", employer.getId());
            information.put("role", "employer");

            return new CustomUserDetails(employer.getEmail(), employer.getPassword(), information, new ArrayList<>());
        }
        throw new UsernameNotFoundException(email);
    }
}
