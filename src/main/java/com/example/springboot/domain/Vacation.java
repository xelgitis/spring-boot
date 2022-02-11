package com.example.springboot.domain;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Vacation {
	
	private Long id;
	private Date startDate;
	private Integer  duration;
	private String approval;
	private String username;
}
