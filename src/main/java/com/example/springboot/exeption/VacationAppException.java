package com.example.springboot.exeption;

public class VacationAppException extends RuntimeException {
	
	//private String message;
	private Status status;
	
	public VacationAppException(Status status) {
		//this.message = message;
		this.status = status;

	}
	
    public Status getStatus() {
        return status;
    }
       
}
