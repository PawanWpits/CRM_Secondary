package com.wpits.services;

import java.util.List;

import com.wpits.dtos.PeriodUnitDto;

public interface PeriodUnitService {
	
	PeriodUnitDto createPeriodUnit(PeriodUnitDto periodUnitDto);
	
	List<PeriodUnitDto> getAllPeriodUnit();
	
	void deletPeriodUnit(int periodId);

}
