package com.wpits.services.serviceImpls;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.CurrencyDto;
import com.wpits.entities.Currency;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.CurrencyRepository;
import com.wpits.services.CurrencyService;

@Service
public class CurrencyServiceImpl implements CurrencyService{
	
	@Autowired
	private CurrencyRepository currencyRepository;
	@Autowired
	private ModelMapper mapper;

	@Override
	public CurrencyDto createCurrency(CurrencyDto currencyDto) {
		
		Currency currency = mapper.map(currencyDto, Currency.class);
		currency.setOptlock(new Random().nextInt(999999));
		Currency savedCurrency = currencyRepository.save(currency);
		
		return mapper.map(savedCurrency, CurrencyDto.class);
	}

	@Override
	public CurrencyDto updateCurrency(CurrencyDto currencyDto, int currencyId) {
		
		Currency currency = currencyRepository.findById(currencyId).orElseThrow( () ->new ResourceNotFoundException("Currency not found with given id !!"));
		
		currency.setSymbol(currencyDto.getSymbol());
		currency.setCode(currencyDto.getCode());
		currency.setCountryCode(currencyDto.getCountryCode());
		currency.setOptlock(currencyDto.getOptlock());
		currency.setDescription(currencyDto.getDescription());
		currency.setRate(currencyDto.getRate());
		currency.setSysRate(currencyDto.getSysRate());
		currency.setInUse(currencyDto.getInUse());
		Currency savedCurrency = currencyRepository.save(currency);
		return mapper.map(savedCurrency, CurrencyDto.class);
	}

	@Override
	public List<CurrencyDto> getAllCurrency() {
		List<Currency> currencies = currencyRepository.findAll();
		List<CurrencyDto> currencyDtos = currencies.stream().map(currency -> mapper.map(currency, CurrencyDto.class)).collect(Collectors.toList());
		return currencyDtos;
	}

	@Override
	public void deleteCurrency(int currencyId) {
		
		Currency currency = currencyRepository.findById(currencyId).orElseThrow( () ->new ResourceNotFoundException("Currency not found with given id !!"));	
		currencyRepository.delete(currency);
	}

}
