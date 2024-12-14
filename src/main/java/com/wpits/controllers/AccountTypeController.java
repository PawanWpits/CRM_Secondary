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

import com.wpits.dtos.AccountTypeDto;
import com.wpits.dtos.ApiResponseMessage;
import com.wpits.services.AccountTypeService;

@RestController
@RequestMapping("/api")
public class AccountTypeController {

	@Autowired
	private AccountTypeService accountTypeService;
	
	@PostMapping("/saveaccount/currency/{currencyId}/entity/{entityId}/language/{languageId}/orderperiod/{orderperiodId}/invoicedelevery/{invoiceDeliveryMethodId}")
	public ResponseEntity<AccountTypeDto> saveAccountType(@RequestBody AccountTypeDto accountTypeDto,@PathVariable int currencyId,@PathVariable int entityId,@PathVariable int languageId,@PathVariable int orderperiodId,@PathVariable int invoiceDeliveryMethodId){
		
		AccountTypeDto accountType = accountTypeService.createAccountType(accountTypeDto, currencyId, entityId, languageId, orderperiodId, invoiceDeliveryMethodId);
		return new ResponseEntity<AccountTypeDto>(accountType,HttpStatus.CREATED);
	}
	
	@PutMapping("/updateaccount/{accountTypeId}")
	public ResponseEntity<AccountTypeDto> updateAccountType(@RequestBody AccountTypeDto accountTypeDto, @PathVariable int accountTypeId){
		
		return ResponseEntity.ok(accountTypeService.updateAccountType(accountTypeDto, accountTypeId));
	}
	
	@GetMapping("/accounts")
	public ResponseEntity<List<AccountTypeDto>> getAllAccountType(){
		
		return ResponseEntity.ok(accountTypeService.getAllAccountType());
	}
	
	@DeleteMapping("/deleteaccount/{accountTypeId}")
	public ResponseEntity<ApiResponseMessage> deleteAccountType(@PathVariable int accountTypeId){
		accountTypeService.deleteAccountType(accountTypeId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
	
}
