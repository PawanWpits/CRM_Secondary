package com.wpits.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.PartnerPaymentsDto;
import com.wpits.services.PartnerPaymentsService;

@RestController
@RequestMapping("/api")
public class PartnerPaymentsController {

	@Autowired
	private PartnerPaymentsService paymentsService;

	@PostMapping("/save/partner/payment/currency/{currencyId}")
	public ResponseEntity<PartnerPaymentsDto> createPayment(@RequestBody PartnerPaymentsDto partnerProductPaymentsDto,
			@PathVariable int currencyId,
			@RequestParam(value = "creditCard", defaultValue = "0") int creditCard,
			@RequestParam(value ="startingNumber", required = false) String startingNumber,
			@RequestParam(value = "endingNumber", required = false) String endingNumber) {
		return new ResponseEntity<PartnerPaymentsDto>(paymentsService.createPayment(partnerProductPaymentsDto, currencyId, creditCard, startingNumber, endingNumber),HttpStatus.CREATED);
	}

	@GetMapping("/partner/payments")
	public ResponseEntity<List<PartnerPaymentsDto>> getAllPartnersPayments() {
		return ResponseEntity.ok(paymentsService.getPayments());
	}
}
