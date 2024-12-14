package com.wpits.services;

import java.util.List;

import com.wpits.dtos.CurrencyDto;

public interface CurrencyService {

	CurrencyDto createCurrency(CurrencyDto currencyDto);
	
	CurrencyDto updateCurrency(CurrencyDto currencyDto, int currencyId);
	
	List<CurrencyDto> getAllCurrency();
	
	void deleteCurrency(int currencyId);
	
	
}
