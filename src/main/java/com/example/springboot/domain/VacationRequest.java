package com.example.springboot.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacationRequest {
	
	@NotNull(message = "start date must be provided")
	private Date startDate;
	@NotNull(message = "duration must be provided")
	private int duration;

}
