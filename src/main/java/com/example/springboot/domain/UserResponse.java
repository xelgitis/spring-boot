package com.example.springboot.domain;

public class UserResponse {
	
	public enum Status {
		SUCCESSFUL,
		ERROR
	}	
	
	String  username;
	Status  status;
	String  message;

    public String getUsername() {
        return username;
    }	
	
    public void setUsername(String username) {
        this.username = username;
    } 	
	
    public Status getStatus() {
        return this.status;
    }	
    
    public void setStatus(Status status) {
        this.status = status;
    }     
    
    public String getMessage() {
        return this.message;
    }	
    
    public void setMessage(String message) {
        this.message = message;
    }      
    
    public UserResponse(Status status) {
        this.status = status;
    }      
	
    public UserResponse(String username, Status status) {
    	this.username = username;
        this.status = status;
    }  
    
    public UserResponse(String username, String message) {
    	this.username = username;
        this.message = message;
    }  
    
    public UserResponse(String username, Status status, String message) {
    	this.username = username;
    	this.status = status;
        this.message = message;
    }  
        
    
	@Override
	public String toString() {
		return "UserResponse{" +
				"username=" + username +
				"status=" + status +
				'}';
	}    

}
