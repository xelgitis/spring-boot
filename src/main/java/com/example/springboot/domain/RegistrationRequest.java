package com.example.springboot.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

	// be sure that username, password and email are provided and not null
	@NotNull(message = "username nije unet")
	@Size(min = 6, max = 25, message = "INVALID_USERNAME_FORMAT")
	private String username;
	@NotNull(message = "password nije unet")
	@Size(min = 8, max = 35, message = "INVALID_PASSWORD_FORMAT")
	private String password;
	private String address;
	private String name;
	@NotNull(message = "email nije unet")
	@Email // this annotation is used for checking format of an email
	private String email;
	@NotNull(message = "role nije unet")
	private String role;

}
