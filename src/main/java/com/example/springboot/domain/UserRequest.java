package com.example.springboot.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequest {

    //TODO: consider to add username and provide username instead of user in the link
	private String address;
	private String name;	
	private String email;
	
	public UserRequest() {}
	
	public UserRequest(String username, String password, String address, String name, String email, String role) {
		this.address  = address;
		this.name = name;
		this.email = email;
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
	
	@Override
	public String toString() {
		return "UserRequest{" +
				", address=" + address +
				", name=" + name +
				", email=" + email +
				'}';
	}	

}
