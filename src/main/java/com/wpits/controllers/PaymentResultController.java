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
import com.wpits.dtos.PaymentResultDto;
import com.wpits.services.PaymentResultService;

@RestController
@RequestMapping("/api")
public class PaymentResultController {

	@Autowired
	private PaymentResultService paymentResultService;
	
	@PostMapping("/savepaymentresult")
	public ResponseEntity<PaymentResultDto> createPaymentResult(@RequestBody PaymentResultDto paymentResultDto){
		return new ResponseEntity<PaymentResultDto>(paymentResultService.createPaymentResult(paymentResultDto),HttpStatus.CREATED);
	}
	
	@GetMapping("/paymentresults")
	public ResponseEntity<List<PaymentResultDto>> getAllPaymentResult(){
		return ResponseEntity.ok(paymentResultService.getAllPaymentResult());
	}
	
	@DeleteMapping("/deletepaymentresults/{paymentResultId}")
	public ResponseEntity<ApiResponseMessage> deletePaymentResult(@PathVariable int paymentResultId){
		paymentResultService.deletePaymentResult(paymentResultId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
}
