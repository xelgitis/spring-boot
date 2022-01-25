package com.example.springboot.domain;

import com.example.springboot.exeption.GenericResponse;

import lombok.Data;

@Data
public class ErrorResponseDto {
	
	private String           message;
	private String           error;
	private GenericResponse  status;

}
