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
import com.wpits.dtos.PaymentMethodDto;
import com.wpits.services.PaymentMethodService;

@RestController
@RequestMapping("/api")
public class PaymentMethodController {

	@Autowired
	private PaymentMethodService paymentMethodService;
	
	@PostMapping("/savepaymentmethod")
	public ResponseEntity<PaymentMethodDto> createPaymentMethod(@RequestBody PaymentMethodDto paymentMethodDto){
		return new ResponseEntity<PaymentMethodDto>(paymentMethodService.createPaymentMethod(paymentMethodDto),HttpStatus.CREATED);
	}
	
	@GetMapping("/paymentmethods")
	public ResponseEntity<List<PaymentMethodDto>> getAllPaymentMethod(){
		return ResponseEntity.ok(paymentMethodService.getALlPaymentMethod());
	}
	
	@DeleteMapping("/deletepaymentmethod/{paymentMethodId}")
	private ResponseEntity<ApiResponseMessage> deletePaymentMethod(@PathVariable int paymentMethodId){
		paymentMethodService.deletePaymentMethod(paymentMethodId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
}
