package com.spring.Final.modules.employee.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
	@NotBlank(message = "Email is required")
	@Email(message = "Email is not valid")
	private String email;

	@NotBlank(message = "Password is required")
	private String password;
}
