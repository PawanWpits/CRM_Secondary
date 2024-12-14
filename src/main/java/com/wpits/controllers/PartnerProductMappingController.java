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
import com.wpits.dtos.PartnerProductMappingDto;
import com.wpits.services.PartnerProductMappingService;

@RestController
@RequestMapping("/api")
public class PartnerProductMappingController {

	@Autowired
	private PartnerProductMappingService partnerProductMappingService;
	
	@PostMapping("/savepartnerproductmapping/partner/{partnerId}")
	public ResponseEntity<PartnerProductMappingDto> createPartnerProductMapping(@RequestBody PartnerProductMappingDto partnerProductMappingDto, @PathVariable int partnerId){
		return new ResponseEntity<PartnerProductMappingDto>(partnerProductMappingService.createPartnerProductMapping(partnerProductMappingDto, partnerId),HttpStatus.CREATED);
	}
	
	@PutMapping("/updatepartnerproductmapping/{partnerProductMappingId}")
	public ResponseEntity<PartnerProductMappingDto> updatePartnerProductMapping(@RequestBody PartnerProductMappingDto partnerProductMappingDto, @PathVariable int partnerProductMappingId){
		return ResponseEntity.ok(partnerProductMappingService.updatePartnerProductMapping(partnerProductMappingDto, partnerProductMappingId));
	}
	
	@GetMapping("/partnerproductMappings")
	public ResponseEntity<List<PartnerProductMappingDto>> getAllPartnerProductMapping(){
		return ResponseEntity.ok(partnerProductMappingService.getAllpartnerProductMapping());
	}
	
	@GetMapping("/partnerProductMapping/{partnerProductMappingId}")
	public ResponseEntity<PartnerProductMappingDto> singlePartnerProductMapping(@PathVariable int partnerProductMappingId){
		return ResponseEntity.ok(partnerProductMappingService.getByIdpartnerProductMapping(partnerProductMappingId));
	}
	
	
	@DeleteMapping("/deletepartnerProductMapping/{partnerProductMappingId}")
	public ResponseEntity<ApiResponseMessage> deletePartnerProductMapping(@PathVariable int partnerProductMappingId){
		partnerProductMappingService.deletepartnerProductMapping(partnerProductMappingId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
}
