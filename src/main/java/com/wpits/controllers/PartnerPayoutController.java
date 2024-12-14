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
import com.wpits.dtos.PartnerPayoutDto;
import com.wpits.services.PartnerPayoutService;

@RestController
@RequestMapping("/api")
public class PartnerPayoutController {

	@Autowired
	private PartnerPayoutService partnerPayoutService;
	
	@PostMapping("/savepartnerpayout/partner/{partnerId}")
	public ResponseEntity<PartnerPayoutDto> savePartnerPayout(@RequestBody PartnerPayoutDto partnerPayoutDto, @PathVariable int partnerId){
		return new ResponseEntity<PartnerPayoutDto>(partnerPayoutService.createPartnerPayout(partnerPayoutDto, partnerId),HttpStatus.CREATED);
	}
	
	@PutMapping("/updatepartnerpayout/{PartnerPayoutId}")
	public ResponseEntity<PartnerPayoutDto> updatePartnerPayout(@RequestBody PartnerPayoutDto partnerPayoutDto, @PathVariable int PartnerPayoutId){
		return ResponseEntity.ok(partnerPayoutService.updatePartnerPayoutDto(partnerPayoutDto, PartnerPayoutId));
	}
	
	@GetMapping("/partnerpayouts")
	public ResponseEntity<List<PartnerPayoutDto>> getAllPartnerPayout(){
		return ResponseEntity.ok(partnerPayoutService.getAllPartnerPayouts());
	}
	
	@DeleteMapping("/deletepartnerpayout/{PartnerPayoutId}")
	public ResponseEntity<ApiResponseMessage> deletePartnerPayout(@PathVariable int PartnerPayoutId){
		partnerPayoutService.deletePartnerPayout(PartnerPayoutId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
}
