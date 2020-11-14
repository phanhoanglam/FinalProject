package com.spring.Final.core.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult implements Serializable {
    private boolean status;
    private Object data;
    private String code = "Ok";
}
