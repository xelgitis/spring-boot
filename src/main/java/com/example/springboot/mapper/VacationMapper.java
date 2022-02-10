package com.example.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.springboot.domain.Vacation;

@Mapper
public interface VacationMapper {
	
	void create(Vacation vacation);
	
	List<Vacation> findVacation(String username);
	
	void updateVacation(Vacation vacation);
	
	void deleteVacation(String username);

}
