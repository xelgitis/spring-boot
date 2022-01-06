package com.example.springboot.domain;

public class LoginResponse {
	
	public enum Status {
		SUCCESS,
		ERROR,
		WRONG_FORMAT_DATA,
		WRONG_PASSWORD
	}

	private String username;	
	private String sessionId;	
	private Status status;
	private String message;

	public LoginResponse(Status status, String message) {
		this.status = status;
		this.message = message;
	}

	public LoginResponse(String username, String sessionId) {
		this.username  = username;
		this.sessionId = sessionId;
	}
	
	public LoginResponse(String username, String sessionId, Status status, String message) {
		this.username  = username;
		this.sessionId = sessionId;
		this.status    = status;
		this.message   = message;
	}	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	
	
	@Override
	public String toString() {
		return "LoginResponse {" +
				"username = " + username +
				" sessionId = " + sessionId +
				" status = " + status +
				" message = " + message +
				'}';
	} 	

}
