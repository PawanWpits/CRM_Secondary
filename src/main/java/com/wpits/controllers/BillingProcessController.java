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
import com.wpits.dtos.BillingProcessDto;
import com.wpits.services.BillingProcessService;

@RestController
@RequestMapping("/api")
public class BillingProcessController {

	@Autowired
	private BillingProcessService billingProcessService;
	
	@PostMapping("/savebillingprocess/periodunit/{periodUnitId}/entity/{entityId}/paperinvoicebatch/{paperInvoiceBatchId}")
	public ResponseEntity<BillingProcessDto> createBillingProcess(@RequestBody BillingProcessDto billingProcessDto, @PathVariable int periodUnitId, @PathVariable int entityId, @PathVariable int paperInvoiceBatchId){
		return new ResponseEntity<BillingProcessDto>(billingProcessService.createBillingProcess(billingProcessDto, periodUnitId, entityId, paperInvoiceBatchId),HttpStatus.CREATED);
	}
	
	@PutMapping("/updatebillingprocess/{billingProcessId}")
	public ResponseEntity<BillingProcessDto> updateBillingProcess(@RequestBody BillingProcessDto billingProcessDto,@PathVariable int billingProcessId){
		return ResponseEntity.ok(billingProcessService.updateBillingProcess(billingProcessDto, billingProcessId));
	}
	
	@GetMapping("/billingprocess")
	public ResponseEntity<List<BillingProcessDto>> getAllBillingProcess(){
		return ResponseEntity.ok(billingProcessService.getAllBillingProcess());
	}
	
	@DeleteMapping("/deletebillingprocess/{billingProcessId}")
	public ResponseEntity<ApiResponseMessage> deleteBillingProcess(@PathVariable int billingProcessId){
		billingProcessService.deletBillingProcess(billingProcessId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
}
