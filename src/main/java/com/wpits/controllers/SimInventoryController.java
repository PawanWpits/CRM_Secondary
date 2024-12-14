package com.wpits.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.PartnerSimResponse;
import com.wpits.dtos.SimInventoryDto;
import com.wpits.helpers.ExcelHelper;
import com.wpits.services.SimInventoryService;

@RestController
@RequestMapping("/api")
public class SimInventoryController {

	@Autowired
	private SimInventoryService simInventoryService;
	
	@PostMapping("/savesiminventory")
	public ResponseEntity<SimInventoryDto> createMsisdnInventory(@Valid @RequestBody SimInventoryDto simInventoryDto){
		return new ResponseEntity<SimInventoryDto>(simInventoryService.createSimInventory(simInventoryDto),HttpStatus.CREATED);
	}
	
	@PutMapping("/updatesiminventory/{simInventoryId}")
	public ResponseEntity<SimInventoryDto> updateMsisdnInventory(@RequestBody SimInventoryDto simInventoryDto, @PathVariable int simInventoryId){
		return ResponseEntity.ok(simInventoryService.updateSimInventory(simInventoryDto, simInventoryId));
	}
	
	@GetMapping("/siminventories")
	public ResponseEntity<List<SimInventoryDto>> getAllMsisdn(){
		return ResponseEntity.ok(simInventoryService.getAllSimInventory());
	}
	
	@GetMapping("/siminventory/{simInventoryId}")
	public ResponseEntity<SimInventoryDto> singleMsisdnInventory(@PathVariable int simInventoryId){
		return ResponseEntity.ok(simInventoryService.getByIdSimInventory(simInventoryId));
	}
	@GetMapping("/siminventory/msisdn/{msisdn}")
	public ResponseEntity<SimInventoryDto> msisdnInventroyfindBymsisdn(@PathVariable String msisdn){
		return ResponseEntity.ok(simInventoryService.findByMsisdn(msisdn));
	}
	
	@DeleteMapping("/deletesiminventory/{simInventoryId}")
	public ResponseEntity<ApiResponseMessage> deleteMsisdnInventory(@PathVariable int simInventoryId){
		simInventoryService.deleteSimInventory(simInventoryId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
	//upload sim by excel vendor
	
	@PostMapping("/sim/upload/excel")
	public ResponseEntity<?> uploadExcel(@RequestParam("file")MultipartFile file,@RequestParam int vendorId){
		
		if (ExcelHelper.checkExcelFormat(file)) {			
			
			simInventoryService.saveExcel(file, vendorId);
			
			return ResponseEntity.ok(Map.of("message","file uploaded successfully !!"));
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please upload excel file !!");
	}

	//assigned sim of partner
	@GetMapping("/assigned/sim/partner/{partnerId}")
	public ResponseEntity<List<PartnerSimResponse>> assignedSimOfPartner(@PathVariable int partnerId){
		return ResponseEntity.ok(simInventoryService.PartnerAssignedSim(partnerId));
	}
	
	@GetMapping("/inventory/product/details/partner/{partnerId}")
	public ResponseEntity<Map<String, Map<String, Long>>> getSimCount(@PathVariable int partnerId){
		return ResponseEntity.ok(simInventoryService.allInventoryProductsCountOfPartner(partnerId));
	}
	
	@GetMapping("/activation/token/msisdn/{msisdn}/code/{activationCode}")
	public ResponseEntity<Map<String, String>> getActivationToken(@PathVariable String msisdn, @PathVariable String activationCode){
		return ResponseEntity.ok(simInventoryService.activationToken(msisdn, activationCode));
	}
	
	@GetMapping("sim/activation/token/{activationToken}/service/{simService}")
	public ResponseEntity<Map<String, String>> simActivationAndDeactivation( @PathVariable String activationToken, @PathVariable String simService){
		return ResponseEntity.ok(simInventoryService.simActivationAndDeactivation(activationToken, simService));
	}
	
    //********************************* Activation flow*******************************************	
	
	//activate by SMS
	@PutMapping("/sim/activate/code/{shortCode}/text/{text}/msisdn/{msisdn}")
	public ResponseEntity<ApiResponseMessage> activateByShortCode(@PathVariable int shortCode, @PathVariable String text, @PathVariable String msisdn){
		return ResponseEntity.ok(simInventoryService.simActivationBySms(shortCode, text, msisdn));
		
	}
	
	//activate by web
	@GetMapping("/get/otp/by/msisdn/{msisdn}")
	public ResponseEntity<Map<String, String>> getActivationCodeOrOtp(@PathVariable String msisdn){
		return ResponseEntity.ok(simInventoryService.getOtpByMsisdn(msisdn));
	}
	
	@PutMapping("/sim/activate/otp/{otp}/msisdn/{msisdn}")
	public ResponseEntity<ApiResponseMessage> activateByWeb(@PathVariable String otp, @PathVariable String msisdn){
		return ResponseEntity.ok(simInventoryService.simActivationByWeb(otp, msisdn));
	}
}
