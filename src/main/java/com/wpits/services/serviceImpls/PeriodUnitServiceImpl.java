package com.wpits.services.serviceImpls;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.PeriodUnitDto;
import com.wpits.entities.PeriodUnit;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.PeriodUnitRepository;
import com.wpits.services.PeriodUnitService;

@Service
public class PeriodUnitServiceImpl implements PeriodUnitService{
	
	@Autowired
	private PeriodUnitRepository periodUnitRepository;
	@Autowired
	private ModelMapper mapper;

	@Override
	public PeriodUnitDto createPeriodUnit(PeriodUnitDto periodUnitDto) {
		
		PeriodUnit periodUnit = mapper.map(periodUnitDto, PeriodUnit.class);
		PeriodUnit savedPeriod = periodUnitRepository.save(periodUnit);
		return mapper.map(savedPeriod, PeriodUnitDto.class);
	}

	@Override
	public List<PeriodUnitDto> getAllPeriodUnit() {
		
		List<PeriodUnit> periods = periodUnitRepository.findAll();
		List<PeriodUnitDto> periodDtos = periods.stream().map( period -> mapper.map(period, PeriodUnitDto.class)).collect(Collectors.toList());
		return periodDtos;
	}

	@Override
	public void deletPeriodUnit(int periodId) {
		
		PeriodUnit periodUnit = periodUnitRepository.findById(periodId).orElseThrow( () -> new ResourceNotFoundException("period unit not found with given id !!"));
		periodUnitRepository.delete(periodUnit);
		
	}

}
