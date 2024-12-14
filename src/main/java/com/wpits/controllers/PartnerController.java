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
import com.wpits.dtos.PartnerDto;
import com.wpits.services.PartnerService;

@RestController
@RequestMapping("/api")
public class PartnerController {

	@Autowired
	private PartnerService partnerService;
	
	@PostMapping("/savepartner/baseuser/{baseUserId}/partnercommission/{partnerCommissionId}")
	public ResponseEntity<PartnerDto> savePartner(@RequestBody PartnerDto partnerDto,@PathVariable int baseUserId, @PathVariable int partnerCommissionId){
		return new ResponseEntity<PartnerDto>(partnerService.createPartner(partnerDto, baseUserId, partnerCommissionId),HttpStatus.CREATED);
	}
	
	@PutMapping("/updatepartner/{partnerId}")
	public ResponseEntity<PartnerDto> updatePartner(@RequestBody PartnerDto partnerDto,@PathVariable int partnerId){
		return ResponseEntity.ok(partnerService.updatePartner(partnerDto, partnerId));
	}
	
	@GetMapping("/partners")
	public ResponseEntity<List<PartnerDto>> getAllPartner(){
		return ResponseEntity.ok(partnerService.getAllPartner());
	}
	
	@GetMapping("/partner/{partnerId}")
	public ResponseEntity<PartnerDto> getSinglePartner(@PathVariable int partnerId){
		return ResponseEntity.ok(partnerService.findByIdPartner(partnerId));
	}
	
	@DeleteMapping("/deletepartner/{partnerId}")
	public ResponseEntity<ApiResponseMessage> deletePartner(@PathVariable int partnerId){
		partnerService.deletePartner(partnerId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully!!").success(true).status(HttpStatus.OK).build());
	}
	
	@GetMapping("/partner/contact/{contact}")
	public ResponseEntity<PartnerDto> getSinglePartner(@PathVariable String contact){
		return ResponseEntity.ok(partnerService.findByPartnerContact(contact));
	}
}
