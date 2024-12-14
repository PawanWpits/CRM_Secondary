package com.wpits.services.serviceImpls;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.CreditCardDto;
import com.wpits.entities.CreditCard;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.CreditCardRepository;
import com.wpits.services.CreditCardService;

@Service
public class CreditCardServiceImpl implements CreditCardService{
	
	@Autowired
	private CreditCardRepository creditCardRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public CreditCardDto createCreditCard(CreditCardDto creditCardDto) {
		CreditCard creditCard = mapper.map(creditCardDto, CreditCard.class);
		creditCard.setOptlock(new Random().nextInt(999999));
		return mapper.map(creditCardRepository.save(creditCard), CreditCardDto.class);
	}

	@Override
	public CreditCardDto updateCreditCard(CreditCardDto creditCardDto, int creditCardId) {
		CreditCard creditCard = creditCardRepository.findById(creditCardId).orElseThrow( () -> new ResourceNotFoundException("credit card not found with given id !!"));
		creditCard.setCcNumber(creditCardDto.getCcNumber());
		creditCard.setCcNumberPlain(creditCardDto.getCcNumberPlain());
		creditCard.setCcExpiry(creditCardDto.getCcExpiry());
		creditCard.setName(creditCardDto.getName());
		creditCard.setCcType(creditCardDto.getCcType());
		creditCard.setDeleted(creditCardDto.getDeleted());
		creditCard.setGatewayKey(creditCardDto.getGatewayKey());
		creditCard.setOptlock(creditCardDto.getOptlock());
		return mapper.map(creditCardRepository.save(creditCard), CreditCardDto.class);
	}

	@Override
	public List<CreditCardDto> getAllCreditCard() {
		return creditCardRepository.findAll().stream().map(creditCard -> mapper.map(creditCard, CreditCardDto.class)).collect(Collectors.toList());
	}

	@Override
	public CreditCardDto findByCreditCradNumber(String number) {
		CreditCard creditCard = creditCardRepository.findByCcNumber(number).orElseThrow( () -> new ResourceNotFoundException("credit card not found with given card number(ccNumber) !!"));
		return mapper.map(creditCard, CreditCardDto.class);
	}
	
	@Override
	public void deleteCreditCard(int creditCardId) {
		CreditCard creditCard = creditCardRepository.findById(creditCardId).orElseThrow( () -> new ResourceNotFoundException("credit card not found with given id !!"));
		creditCardRepository.delete(creditCard);
	}



}
