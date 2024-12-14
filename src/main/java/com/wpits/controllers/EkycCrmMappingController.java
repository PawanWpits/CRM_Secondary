package com.wpits.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.EkycCrmMappingDto;
import com.wpits.services.EkycCrmMappingService;

@RestController
@RequestMapping("/api")
public class EkycCrmMappingController {

	@Autowired
	private EkycCrmMappingService ekycCrmMappingService;
	
	@PostMapping("/save/ekyc/customer/in/crm")
	public ResponseEntity<EkycCrmMappingDto> saveEkycCustomerInCrm(@RequestBody EkycCrmMappingDto ekycCrmMappingDto){
		return new ResponseEntity<EkycCrmMappingDto>(ekycCrmMappingService.createCustomerFromEkyc(ekycCrmMappingDto),HttpStatus.CREATED);
	}
	
	@PostMapping("/save/ekyc/device")
	public ResponseEntity<ApiResponseMessage> saveDeviceKycRecordInCrm(
			@RequestParam(value = "token", required = true)String token,
			@RequestParam(value = "deviceId")int deviceId,
			@RequestParam(value = "partnerId", defaultValue = "0") int partnerId)
			{
				return new ResponseEntity<ApiResponseMessage>(ekycCrmMappingService.createCustomerFromKycForDevice(token, deviceId, partnerId),HttpStatus.CREATED);
	}
	
	
}
