package com.spring.Final.modules.notification.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SetReadDTO implements Serializable {
    @NotNull(message = "Ids are required")
    private int[] ids;
}
