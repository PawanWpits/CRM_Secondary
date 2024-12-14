package com.wpits.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.CustomerInvoiceDto;
import com.wpits.services.CustomerInvoiceService;

@RestController
@RequestMapping("/api")
public class CustomerInvoiceController {

	@Autowired
	private CustomerInvoiceService customerInvoiceService;
	
	//custInvoice save by order service direct
	
	@GetMapping("/invoice/order/{orderNumber}")
	public ResponseEntity<CustomerInvoiceDto> getInvoice(@PathVariable String orderNumber){
		return ResponseEntity.ok(customerInvoiceService.findByOrderNumber(orderNumber));
	}
}
