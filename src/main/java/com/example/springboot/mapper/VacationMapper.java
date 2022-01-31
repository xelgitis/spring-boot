package com.example.springboot.mapper;

import java.util.Date;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.springboot.domain.Vacation;

@Mapper
public interface VacationMapper {
	
	void create(Vacation vacation);
	
	Optional<Vacation> findVacation(String username);
	
	void updateVacation(Date startDate, int duration, String username);
	
	void deleteVacation(String username);

}
