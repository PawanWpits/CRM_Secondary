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
import com.wpits.dtos.PartnerCommissionDto;
import com.wpits.services.PartnerCommissionService;

@RestController
@RequestMapping("/api")
public class PartnerCommissionController {

	@Autowired
	private PartnerCommissionService partnerCommissionService;
	
	@PostMapping("/savepartnercommission/currency/{currencyId}")
	public ResponseEntity<PartnerCommissionDto> savePartnerCommission(@RequestBody PartnerCommissionDto partnerCommissionDto, @PathVariable int currencyId){
		return new ResponseEntity<PartnerCommissionDto>(partnerCommissionService.createPartnerCommission(partnerCommissionDto, currencyId),HttpStatus.CREATED);
	}
	
	@PutMapping("/updatePartnerCommission/{partnerCommissionId}")
	public ResponseEntity<PartnerCommissionDto> updatePartnerCommission(@RequestBody PartnerCommissionDto partnerCommissionDto, @PathVariable int partnerCommissionId){
		return ResponseEntity.ok(partnerCommissionService.updatePartnerCommission(partnerCommissionDto, partnerCommissionId));
	}
	
	@GetMapping("/partnercommissions")
	public ResponseEntity<List<PartnerCommissionDto>>  getAllPartnerCommission(){
		return ResponseEntity.ok(partnerCommissionService.getAllPartnerCommission());
	}
	
	@GetMapping("/partnercommission/{partnerCommissionId}")
	public ResponseEntity<PartnerCommissionDto> getSinglePartnerCommission(@PathVariable int partnerCommissionId){
		return ResponseEntity.ok(partnerCommissionService.findByIdPartnerCommission(partnerCommissionId));
	}
	
	@DeleteMapping("/deletepartnercommission/{partnerCommissionId}")
	public ResponseEntity<ApiResponseMessage> deletePartnerCommission(@PathVariable int partnerCommissionId){
		partnerCommissionService.deletePartnerCommision(partnerCommissionId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("delete successfully !!").success(true).status(HttpStatus.OK).build());
	}
		
	//get commision partnerId based
	
	@GetMapping("/commission/partner/{partnerId}")
	public ResponseEntity<List<Map<String, Object>>> getCommisionByPartnerId(@PathVariable int partnerId){
		return ResponseEntity.ok(partnerCommissionService.getCommissionByPartnerId(partnerId));
	}
}
