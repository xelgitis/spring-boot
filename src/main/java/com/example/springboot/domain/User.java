package com.example.springboot.domain;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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
        private Role role;
		private Date registrationTime;
		private Date loginTime;	
}
