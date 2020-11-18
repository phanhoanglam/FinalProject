package com.spring.Final.modules.employee.dtos;

import com.spring.Final.modules.shared.dtos.SearchDTO;
import lombok.Data;

@Data
public class SearchEmployeeDTO extends SearchDTO {
    private float salaryFrom = 0;
    private float salaryTo = 250;

    public SearchEmployeeDTO() {
        super();
    }
}
