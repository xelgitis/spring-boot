package com.example.springboot.exeption;

public class VacationAppException extends RuntimeException {
	
	private String message;
	private Status status;
	
	public VacationAppException(String message, Status status) {
		this.message = message;
		this.status = status;

	}
	
    public String getMessage() {
        return message;
    }
    
    public Status getStatus() {
        return status;
    }
       
}
