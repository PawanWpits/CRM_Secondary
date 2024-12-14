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
import com.wpits.dtos.VendorDto;
import com.wpits.services.VendorService;

@RestController
@RequestMapping("/api")
public class VendorController {

	@Autowired
	private VendorService vendorService;
	
	@PostMapping("/save/vendor")
	public ResponseEntity<VendorDto> createVendor(@RequestBody VendorDto vendorDto){
		return new ResponseEntity<VendorDto>(vendorService.createVendor(vendorDto),HttpStatus.CREATED);
	}
	
	@PutMapping("/update/vendor/vendorId/{vendorId}")
	public ResponseEntity<VendorDto> updateVendor(@PathVariable int vendorId, @RequestBody VendorDto vendorDto){
		return ResponseEntity.ok(vendorService.updateVendor(vendorId, vendorDto));
	}
	
	@GetMapping("/vendors")
	public ResponseEntity<List<VendorDto>> getVendors(){
		return ResponseEntity.ok(vendorService.getVendors());
	}
	
	@GetMapping("/vendor/vendorId/{vendorId}")
	public ResponseEntity<VendorDto> getVendor(@PathVariable int vendorId){
		return ResponseEntity.ok(vendorService.getByIdVendor(vendorId));
	}
	
	@DeleteMapping("/delete/vendor/vendorId/{vendorId}")
	public ResponseEntity<ApiResponseMessage> deleteVendor(@PathVariable int vendorId){
		vendorService.deleteVendor(vendorId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
}
