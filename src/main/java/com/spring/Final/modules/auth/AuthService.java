package com.spring.Final.modules.auth;

import com.spring.Final.core.common.MapUtils;
import com.spring.Final.modules.auth.dtos.RegisterDTO;
import com.spring.Final.modules.employee.EmployeeService;
import com.spring.Final.modules.employer.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {
    @Autowired
    EmployerService employerService;

    @Autowired
    EmployeeService employeeService;

    public void register(RegisterDTO dto, String accountType) throws IOException {
        dto.setAddressLocation(MapUtils.getCoordinateByText(dto.getAddress()));

        if (accountType.equals("employer")) {
            this.employerService.register(dto);
        } else {
            this.employeeService.register(dto);
        }
    }
}
