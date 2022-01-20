package com.example.springboot.domain;

public class VacationResponse {
	
	public enum Status {
		SUCCESS,
		ERROR,
	}

	private Status status;
	private String message;
	
	public VacationResponse(Status status, String message) {
		this.status  = status;
		this.message = message;
	}	

	public VacationResponse(Status status) {
		this.status = status;
	}	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}	
	
	@Override
	public String toString() {
		return "VacationResponse{" +
				", status=" + status +
				", message=" + message +
				'}';
	}	

}
