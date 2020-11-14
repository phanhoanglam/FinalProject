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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        boolean isEmployee = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUriString().contains("/employees/login");
        EmployeeEntity employee = null;
        EmployerEntity employer = null;

        if (isEmployee) {
            employee = employeeRepository.findByEmail(email);
        } else {
            employer = employerRepository.findByEmail(email);
        }

        if (isEmployee) {
            if (employee != null) {
                return new org.springframework.security.core.userdetails.User(employee.getEmail(), employee.getPassword(), new ArrayList<>());
            }
            throw new UsernameNotFoundException(email);
        }
        if (employer != null) {
            return new org.springframework.security.core.userdetails.User(employer.getEmail(), employer.getPassword(), new ArrayList<>());
        }
        throw new UsernameNotFoundException(email);
    }

    private UserDetails buildUserForAuthentication(EmployeeEntity employee) {
        return new org.springframework.security.core.userdetails.User(employee.getEmail(), employee.getPassword(), new ArrayList<>());
    }
}
