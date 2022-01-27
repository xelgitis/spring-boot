package com.example.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VacationResponse {
	
	private ResponseStatus status;
	private String         message;

}
