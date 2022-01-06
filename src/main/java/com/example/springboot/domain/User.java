package com.example.springboot.domain;
import java.util.Date;

public class User {

		private Long id;
		private String username;
		private String password;
		private String passwordSalt;
		private String hashedPassword;	
		private String address;
		private String name;
		private String email;
        private String role;
		private Date registrationTime;
		
		public User() {}
		
		public User(String username, String password, String passwordSalt, String hashedPassword, String address, String name, String email, String role, Date registrationTime) {
			this.username = username;
			this.password = password; 
			this.passwordSalt = passwordSalt;
			this.hashedPassword = hashedPassword;
			this.address = address;
			this.name = name;
			this.email = email;
			this.role = role;
			this.registrationTime = registrationTime;
		}
		
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public String getUsername() {
			return username;
		}	
		
		public void setUsername(String username) {
			this.username = username;
		}		
		
		public String getPassword() {
			return password;
		}	
		
		public void setPassword(String password) {
			this.password = password;
		}			
		
		public String getPasswordSalt() {
			return passwordSalt;
		}	
		
		public void setPasswordSalt(String passwordSalt) {
			this.passwordSalt = passwordSalt;
		}	
		
		public String getHashedPassword() {
			return hashedPassword;
		}	
		
		public void setHashedPassword(String hashedPassword) {
			this.hashedPassword = hashedPassword;
		}	
		
		public String getAddress() {
			return address;
		}	
		
		public void setAddress(String address) {
			this.address = address;
		}			
		
		public String getName() {
			return name;
		}	
		
		public void setName(String name) {
			this.name = name;
		}		
		
		public String getEmail() {
			return email;
		}	
		
		public void setEmail(String email) {
			this.email = email;
		}	
		
		public String getRole() {
			return role;
		}	
		
		public void setRole(String role) {
			this.role = role;
		}			
		
		public Date getRegistrationTime() {
			return registrationTime;
		}	
		
		public void setRegistrationTime(Date registrationTime) {
			this.registrationTime = registrationTime;
		}			

		@Override
		public String toString() {
			return "User{" +
					"id=" + id +
					", username='" + username + '\'' +
					//", password='" + password + '\'' +
					//", passwordSalt='" + passwordSalt + '\'' +
					//", hashedPassword='" + hashedPassword + '\'' +
					", address='" + address + '\'' +
					", name='" + name + '\'' +
					", email='" + email + '\'' +
					", role='" + role + '\'' +
					", registrationTime=" + registrationTime +
					'}';
		}	
	
}
