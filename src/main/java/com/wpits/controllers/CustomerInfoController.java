package com.wpits.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.services.CustomerInfoService;

@RestController
@RequestMapping("/api")
public class CustomerInfoController {

	@Autowired
	private CustomerInfoService customerInfoService;
	
	@PostMapping("/savecustomerinfo")
	public ResponseEntity<ApiResponseMessage> saveCustomerPaymentInfo(){
		customerInfoService.saveCustoomerInfo();
		return ResponseEntity.ok(ApiResponseMessage.builder().message("saved successfully !!").success(true).status(HttpStatus.OK).build());		
	}
}
