package com.wpits.controllers;

import java.util.List;
import java.util.Map;

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
import com.wpits.dtos.InvoiceDto;
import com.wpits.services.InvoiceService;

@RestController
@RequestMapping("/api")
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;
	
	@PostMapping("/saveinvoice/billingprocess/{billingProcessId}/paperinvoice/{paperInvoiceId}/Currency/{currencyId}")
	public ResponseEntity<InvoiceDto> createInvoice(@RequestBody InvoiceDto invoiceDto, @PathVariable int billingProcessId, @PathVariable int paperInvoiceId, @PathVariable int currencyId){
		return new ResponseEntity<InvoiceDto>(invoiceService.createInvoice(invoiceDto, billingProcessId, paperInvoiceId, currencyId),HttpStatus.CREATED);
	}
	
	@PutMapping("/updateinvoice/{invoiceId}")
	public ResponseEntity<InvoiceDto> updateInvoice(@RequestBody InvoiceDto invoiceDto, @PathVariable int invoiceId){
		return ResponseEntity.ok(invoiceService.updateInvoice(invoiceDto, invoiceId));
	}
	
	@GetMapping("/invoices")
	public ResponseEntity<List<InvoiceDto>> getAllInvoices(){
		return ResponseEntity.ok(invoiceService.getAllInvoice());
	}
	
	@GetMapping("/invoice/{invoiceId}")
	public ResponseEntity<InvoiceDto> singleInvoice(@PathVariable int invoiceId){
		return ResponseEntity.ok(invoiceService.findByIdInvoice(invoiceId));
	}
	
	@DeleteMapping("/deleteinvoice/{invoiceId}")
	public ResponseEntity<ApiResponseMessage> deleteInvoice(@PathVariable int invoiceId){
		invoiceService.deleteInvoice(invoiceId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
	@GetMapping("/invoice")
	public void genrateInvoice() {
		invoiceService.genrateInvoice();
	}
	
	//
	@GetMapping("/invoice/details")
	public ResponseEntity<Map<String, Object>> invoicesDetails(){
		return ResponseEntity.ok(invoiceService.invoiceDetails());
	}
	
	@GetMapping("invoice/list/msisdn/{msisdn}")
	public ResponseEntity<List<InvoiceDto>> invoiceGetByMsisdn(@PathVariable String msisdn){
		return ResponseEntity.ok(invoiceService.findInvoiceByMsisdn(msisdn));
	}
	
	@GetMapping("/invoice/msisdn/{msisdn}")
	public ResponseEntity<InvoiceDto> findUnpaidInvoiceByMsisdn(@PathVariable String msisdn){
		return ResponseEntity.ok(invoiceService.findUnpaidInviceByMsisdn(msisdn));
	}
}
