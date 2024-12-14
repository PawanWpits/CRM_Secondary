package com.wpits.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.PeriodUnitDto;
import com.wpits.services.PeriodUnitService;

@RestController
@RequestMapping("/api")
public class PeriodUnitController {
	@Autowired
	private PeriodUnitService periodUnitService;
	
	@PostMapping("/saveperiodunit")
	public ResponseEntity<PeriodUnitDto> savePeriodUnit(@RequestBody PeriodUnitDto periodUnitDto){
		
		PeriodUnitDto periodUnit = periodUnitService.createPeriodUnit(periodUnitDto);
		return new ResponseEntity<PeriodUnitDto>(periodUnit,HttpStatus.CREATED);
	}
	
	@GetMapping("/allperiodunit")
	public ResponseEntity<List<PeriodUnitDto>> getAllPeriodUnit(){
		
		List<PeriodUnitDto> periodUnit = periodUnitService.getAllPeriodUnit();
		return ResponseEntity.ok(periodUnit);
	}
	
	@DeleteMapping("/deleteperiodunit/{periodId}")
	public ResponseEntity<ApiResponseMessage> deletePeriodUnit(@PathVariable int periodId){
		
		periodUnitService.deletPeriodUnit(periodId);
		ApiResponseMessage message = ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build();
		return ResponseEntity.ok(message);
	}
	
}
