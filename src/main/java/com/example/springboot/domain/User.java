package com.example.springboot.domain;
import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
		
}
