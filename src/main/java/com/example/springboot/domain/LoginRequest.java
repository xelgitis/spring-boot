package com.example.springboot.domain;

import javax.validation.constraints.NotNull;

public class LoginRequest {

	@NotNull(message = "Username se mora uneti")
	private String username;
	@NotNull(message = "Password se mora uneti")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override public String toString() {
		return "LoginRequest{" +
				"username='" + username + '\'' +
				", password=" + password +
				'}';
	}
}

