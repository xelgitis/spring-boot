package com.example.springboot.mapper;

import java.util.Date;
import org.apache.ibatis.annotations.Mapper;

import com.example.springboot.domain.Vacation;

@Mapper
public interface VacationMapper {
	
	void create(Vacation vacation);
	
	Vacation findVacationByUniqueUsername(String username);
	
	void updateVacationByUniqueUsername(Date startDate, int duration, String username);
	
	void deleteVacationByUniqueUsername(String username);

}
