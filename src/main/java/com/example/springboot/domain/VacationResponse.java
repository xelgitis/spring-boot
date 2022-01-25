package com.example.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VacationResponse {
	
	public enum Status {
		SUCCESS,
		ERROR,
	}

	private Status status;
	private String message;

}
