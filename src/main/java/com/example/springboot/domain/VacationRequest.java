package com.example.springboot.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class VacationRequest {
	
	@NotNull(message = "start date must be provided")
	private Date startDate;
	@NotNull(message = "duration must be provided")
	private int duration;
	
	public VacationRequest() {}
	
	public VacationRequest(Date startDate, int duration) {
		this.startDate = startDate;
		this.duration = duration;
	}	
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	@Override
	public String toString() {
		return "VacationRequest{" +
				", startDate=" + startDate +
				", duration=" + duration +
				'}';
	}		

}
