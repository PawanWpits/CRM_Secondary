package com.wpits.services;

import java.util.List;

import com.wpits.dtos.CreditCardDto;

public interface CreditCardService {
	
	CreditCardDto createCreditCard(CreditCardDto creditCardDto);
	
	CreditCardDto updateCreditCard(CreditCardDto creditCardDto,int creditCardId);
	
	List<CreditCardDto> getAllCreditCard();
	
	CreditCardDto findByCreditCradNumber(String number);  //ccNumber
	
	void deleteCreditCard(int creditCardId);

}
