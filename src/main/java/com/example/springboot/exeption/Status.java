package com.example.springboot.exeption;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {

	SUCCESS("SUCCESS"),
	USERNAME_TAKEN("USERNAME_TAKEN"),
	EMAIL_TAKEN("EMAIL_TAKEN"),	
	GENERIC_ERROR("GENERIC_ERROR"),
	USER_NOT_FOUND("USER_NOT_FOUND"),
	WRONG_FORMAT_DATA("WRONG_FORMAT_DATA"),
	VACATION_NOT_PRESENT("VACATION_NOT_PRESENT"),
	WRONG_PASSWORD("WRONG_PASSWORD");

	private final String jsonValue;

	Status(String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonValue
	public String jsonValue() {
		return jsonValue;
	}
}
