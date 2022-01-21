package com.example.springboot.domain;

import javax.validation.constraints.NotNull;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

	@NotNull(message = "Username se mora uneti")
	private String username;
	@NotNull(message = "Password se mora uneti")
	private String password;

}

