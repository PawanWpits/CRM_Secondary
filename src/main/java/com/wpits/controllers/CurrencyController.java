package com.wpits.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.CurrencyDto;
import com.wpits.services.CurrencyService;

@RestController
@RequestMapping("/api")
public class CurrencyController {

	@Autowired
	private CurrencyService currencyService;
	
	@PostMapping("/savecurrency")
	public ResponseEntity<CurrencyDto> saveCurrency(@RequestBody CurrencyDto currencyDto){
		
		CurrencyDto currency = currencyService.createCurrency(currencyDto);
		return new ResponseEntity<CurrencyDto>(currency,HttpStatus.CREATED);
	}
	
	@PutMapping("updatecurrency/{currencyId}")
	public ResponseEntity<CurrencyDto> updateCurrency(@RequestBody CurrencyDto currencyDto,@PathVariable int currencyId){
		
		CurrencyDto currency = currencyService.updateCurrency(currencyDto, currencyId);	
		return ResponseEntity.ok(currency);
	}
	
	@GetMapping("/allcurrency")
	public ResponseEntity<List<CurrencyDto>> gatAllCurrency(){
		
		List<CurrencyDto> curriency = currencyService.getAllCurrency();
		return ResponseEntity.ok(curriency);
	}
	
	@DeleteMapping("/deletecurrency/{currencyId}")
	public ResponseEntity<ApiResponseMessage> deleteCurrency(@PathVariable int currencyId){
		
		currencyService.deleteCurrency(currencyId);
		
		ApiResponseMessage message = ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build();
		return ResponseEntity.ok(message);	
	}
	
	
}
