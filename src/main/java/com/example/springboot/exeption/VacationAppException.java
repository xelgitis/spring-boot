package com.example.springboot.exeption;

public class VacationAppException extends RuntimeException {
	
	private String message;
	private GenericResponse status;
	
	public VacationAppException(String message, GenericResponse status) {
		this.message = message;
		this.status = status;

	}
	
    public String getMessage() {
        return message;
    }
    
    public GenericResponse getStatus() {
        return status;
    }
       
}
