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
	
	@NotNull(message = "mora se uneti pocetak odmora")
	private Date startDate;
	@NotNull(message = "mora se uneti trajanje odmora")
	private Integer duration;

}
