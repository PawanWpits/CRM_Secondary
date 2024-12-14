package com.wpits.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.CreditCardDto;
import com.wpits.services.CreditCardService;

@RestController
@RequestMapping("/api")
public class CreditCardController {

	@Autowired
	private CreditCardService creditCardService;
	
	@PostMapping("/savecreditcard")
	public ResponseEntity<CreditCardDto> saveCrediCard(@RequestBody CreditCardDto creditCardDto){
		return new ResponseEntity<CreditCardDto>(creditCardService.createCreditCard(creditCardDto),HttpStatus.CREATED);
	}
	
	@PutMapping("/updatecreditcard/{creditCardId}")
	public ResponseEntity<CreditCardDto> updateCrediCard(@RequestBody CreditCardDto creditCardDto,@PathVariable int creditCardId){
		return ResponseEntity.ok(creditCardService.updateCreditCard(creditCardDto, creditCardId));
	}
	
	@GetMapping("/creditcards")
	public ResponseEntity<List<CreditCardDto>> getAllCreditCardDetail(){
		return ResponseEntity.ok(creditCardService.getAllCreditCard());
	}
	
	@GetMapping("/creditcard/ccnumber/{number}")
	public ResponseEntity<CreditCardDto> getCreditCardByNumber(@PathVariable String number){
		return ResponseEntity.ok(creditCardService.findByCreditCradNumber(number));
	}
	
	@DeleteMapping("/deletecreditcard/{creditCardId}")
	public ResponseEntity<ApiResponseMessage> deleteCreditCard(@PathVariable int creditCardId){
		creditCardService.deleteCreditCard(creditCardId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
}
