package com.example.springboot.exeption;

public class LoginException extends RuntimeException {
	
	private String loginStatus;
	
	public LoginException(String loginStatus) {
		this.loginStatus = loginStatus;
	}
	
    public String getLoginStatus() {
        return loginStatus;
     }    
    
	@Override public String toString() {
		return "LoginException{" +
				"loginStatus='" + loginStatus + '\'' +
				'}';
	}	

}
