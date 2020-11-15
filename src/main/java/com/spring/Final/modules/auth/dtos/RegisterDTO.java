package com.spring.Final.modules.auth.dtos;

import lombok.Data;
import org.locationtech.jts.geom.Coordinate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class RegisterDTO implements Serializable {
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

//    @NotBlank(message = "First name is required")
    private String firstName;

//    @NotBlank(message = "Last name is required")
    private String lastName;

//    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;

    private Coordinate addressLocation;
}
