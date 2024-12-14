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
import com.wpits.dtos.InvoiceDeliveryMethodDto;
import com.wpits.services.InvoiceDeliveryMethodService;

@RestController
@RequestMapping("/api")
public class InvoiceDeliveryMethodontroller {

	@Autowired
	private InvoiceDeliveryMethodService invoiceDeliveryMethodService;
	
	@PostMapping("/invoicedelevery")
	public ResponseEntity<InvoiceDeliveryMethodDto> saveInvoiceDelevery(@RequestBody InvoiceDeliveryMethodDto invoiceDeliveryMethodDto){
		
		InvoiceDeliveryMethodDto invoiceDelevery = invoiceDeliveryMethodService.createInvoiceDelevery(invoiceDeliveryMethodDto);
		return new ResponseEntity<InvoiceDeliveryMethodDto>(invoiceDelevery,HttpStatus.CREATED);
	}
	
	@GetMapping("/allinvoicedelevery")
	public ResponseEntity<List<InvoiceDeliveryMethodDto>> getAllInvoiceDevlevery(){
		
		List<InvoiceDeliveryMethodDto> invoiceDelevery = invoiceDeliveryMethodService.getAllInvoiceDelevery();
		return ResponseEntity.ok(invoiceDelevery);
	}
	
	@DeleteMapping("/deleteinvoicedelevery/{invoiceId}")
	public ResponseEntity<ApiResponseMessage> deleteInvoiceDelevery(@PathVariable int invoiceId){
		invoiceDeliveryMethodService.deleteInvoiceDelevery(invoiceId);
		
		ApiResponseMessage message = ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build();
		return ResponseEntity.ok(message);
	}
	
	
}

