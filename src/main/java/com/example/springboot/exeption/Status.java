package com.example.springboot.exeption;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {

	SUCCESS("SUCCESS", HttpStatus.OK),
	USERNAME_TAKEN("USERNAME_TAKEN", HttpStatus.CONFLICT),
	EMAIL_TAKEN("EMAIL_TAKEN", HttpStatus.CONFLICT),	
	GENERIC_ERROR("GENERIC_ERROR", HttpStatus.BAD_REQUEST),
	USER_NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND),
	WRONG_FORMAT_DATA("WRONG_FORMAT_DATA", HttpStatus.UNAUTHORIZED),
	VACATION_NOT_PRESENT("VACATION_NOT_PRESENT", HttpStatus.BAD_REQUEST),
	USER_NOT_LOGGED_IN("USER_NOT_LOGGED_IN", HttpStatus.NOT_FOUND),
	USER_DOES_NOT_HAVE_PRIVLEGES("USER_DOES_NOT_HAVE_PRIVLEGES", HttpStatus.FORBIDDEN),
	USER_NOT_ADMIN("USER_NOT_ADMIN", HttpStatus.FORBIDDEN),
	VACATION_DATE_IN_THE_PAST("VACATION_DATE_IN_THE_PAST", HttpStatus.NOT_ACCEPTABLE),
	INVALID_LENGTH("INVALID_LENGTH", HttpStatus.LENGTH_REQUIRED),
	WRONG_PASSWORD("WRONG_PASSWORD", HttpStatus.UNAUTHORIZED);

	private final String     jsonValue;
	private final HttpStatus httpStatus;

	Status(String jsonValue, HttpStatus conflict) {
		this.jsonValue  = jsonValue;
		this.httpStatus = conflict;
	}

	@JsonValue
	public String jsonValue() {
		return jsonValue;
	}
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}	
}
