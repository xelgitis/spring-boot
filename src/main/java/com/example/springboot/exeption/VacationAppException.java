package com.example.springboot.exeption;

public class VacationAppException extends RuntimeException {

	private Status status;
	
	public VacationAppException(Status status) {
		this.status = status;

	}
	
    public Status getStatus() {
        return status;
    }
       
}
