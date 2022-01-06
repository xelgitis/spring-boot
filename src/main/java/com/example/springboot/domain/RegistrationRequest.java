package com.example.springboot.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegistrationRequest {

	// be sure that username, password and email are provided and not null
	@NotNull(message = "Username se mora uneti")
	@Size(min = 6, max = 25, message = "INVALID_USERNAME_FORMAT")
	private String username;
	@NotNull(message = "Password se mora uneti")
	@Size(min = 8, max = 35, message = "INVALID_PASSWORD_FORMAT")
	private String password;
	private String address;
	private String name;
	@NotNull(message = "Email se mora uneti")
	@Email // this annotation is used for checking format of an email
	private String email;
	@NotNull(message = "Role se mora uneti")
	private String role;

	public RegistrationRequest() {
	}

	public RegistrationRequest(String username, String password, String address, String name, String email,
			                   String role) {
		this.username = username;
		this.password = password;
		this.address = address;
		this.name = name;
		this.email = email;
		this.role = role;
	}

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "RegistrationRequest{" + "username='" + username + '\'' + ", address=" + address + ", name=" + name + ", email="
				+ email + ", role=" + role + '}';
	}
}
