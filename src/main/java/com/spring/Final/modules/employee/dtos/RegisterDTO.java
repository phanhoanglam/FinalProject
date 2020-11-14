package com.spring.Final.modules.employee.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;

@Data
public class RegisterDTO implements Serializable {
	@NotBlank(message = "Email is required")
	@Email(message = "Email is not valid")
	private String email;

	@NotBlank(message = "Password is required")
	private String password;

	@NotBlank(message = "First name is required")
	private String firstName;

	@NotBlank(message = "Last name is required")
	private String lastName;

	@NotBlank(message = "Phone is required")
	private String phone;

	@NotBlank(message = "Address is required")
	private String address;

	@NotNull(message = "Address location is required")
	private HashMap<String, String> addressLocation;
}
