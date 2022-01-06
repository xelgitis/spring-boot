package com.example.springboot.exeption;

public class RegistrationException extends RuntimeException {
	
	private String registrationStatus;
	
	public RegistrationException(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}
	
    public String getRegistrationStatus() {
        return registrationStatus;
     }    
    
	@Override public String toString() {
		return "RegistrationException{" +
				"registrationStatus='" + registrationStatus + '\'' +
				'}';
	}

}
