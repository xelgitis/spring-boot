package com.example.springboot.domain;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vacation {
	
	private Long id;
	private Date startDate;
	private int  duration;
	private char approval;
	private String username;	
	
	//public Vacation() {}
	
	/*public Vacation(Date startDate, int duration, char approval, String username) {
		this.startDate = startDate;
		this.duration = duration;
		this.approval = approval;
		this.username = username;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public char getApproval() {
		return approval;
	}
	public void setApproval(char approval) {
		this.approval = approval;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}	
	
	@Override
	public String toString() {
		return "Vacation{" +
				"startDate='" + startDate + '\'' +
				", duration=" + duration +
				", approval=" + approval +
				", username=" + username +
				'}';
	}		*/
}
