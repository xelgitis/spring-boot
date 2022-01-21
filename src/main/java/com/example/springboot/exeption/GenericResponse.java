package com.example.springboot.exeption;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GenericResponse {

	OK("OK"),
	USERNAME_TAKEN("USERNAME_TAKEN"),
	EMAIL_TAKEN("EMAIL_TAKEN"),	
	GENERIC_ERROR("GENERIC_ERROR"),
	USER_NOT_FOUND("USER_NOT_FOUND"),
	WRONG_PASSWORD("WRONG_PASSWORD");

	private final String jsonValue;

	GenericResponse(String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonValue
	public String jsonValue() {
		return jsonValue;
	}
}
