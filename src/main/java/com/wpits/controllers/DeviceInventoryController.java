package com.wpits.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.DeviceInventoryDto;
import com.wpits.dtos.ProductResponse;
import com.wpits.dtos.PartnerSimResponse;
import com.wpits.helpers.ExcelHelper;
import com.wpits.services.DeviceInventoryService;

@RestController
@RequestMapping("/api")
public class DeviceInventoryController {

	@Autowired
	private DeviceInventoryService deviceInventoryService;
	
	@Autowired
	private ObjectMapper mapper;
	
	private Logger logger = LoggerFactory.getLogger(DeviceInventoryController.class);
	
	@PostMapping("/savedeviceinventory")
	public ResponseEntity<DeviceInventoryDto> createDeviceInventory(@RequestBody DeviceInventoryDto deviceInventoryDto){
		return new ResponseEntity<DeviceInventoryDto>(deviceInventoryService.createDeviceInventory(deviceInventoryDto),HttpStatus.CREATED);
	}
	
	@PutMapping("/updatedeviceinventory/{deviceInventoryId}")
	public ResponseEntity<DeviceInventoryDto> updateDeviceInventory(@RequestBody DeviceInventoryDto deviceInventoryDto,@PathVariable int deviceInventoryId){
		return ResponseEntity.ok(deviceInventoryService.updateDeviceInventory(deviceInventoryDto, deviceInventoryId));
	}
	
	@GetMapping("/deviceinventories")
	public ResponseEntity<List<DeviceInventoryDto>> getAllDeviceInventory(){
		return ResponseEntity.ok(deviceInventoryService.getAllDeviceInventory());
	}
	
	@GetMapping("/deviceinventory/{deviceInventoryId}")
	public ResponseEntity<DeviceInventoryDto> singleDeviceInventory(@PathVariable int deviceInventoryId){
		return ResponseEntity.ok(deviceInventoryService.getByIdDeviceInventory(deviceInventoryId));
	}
	
	@DeleteMapping("/deletedeviceinventory/{deviceInventoryId}")
	public ResponseEntity<ApiResponseMessage> deleteDeviceInventory(@PathVariable int deviceInventoryId){
		deviceInventoryService.deleteDeviceInventory(deviceInventoryId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
	
	//upload device by excel vendor
	
		@PostMapping("/device/upload/excel")
		public ResponseEntity<?> uploadExcel(@RequestParam("file")MultipartFile file,@RequestParam int vendorId){
			
			if (ExcelHelper.checkExcelFormat(file)) {			
				
				deviceInventoryService.saveExcel(file, vendorId);
				
				return ResponseEntity.ok(Map.of("message","file uploaded successfully !!"));
			}
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please upload excel file !!");
		}
		
		
		//assigned device of partner
		
		@GetMapping("/assigned/device/partner/{partnerId}")
		public ResponseEntity<List<ProductResponse>> assignedSimOfPartner(@PathVariable int partnerId) {
			return ResponseEntity.ok(deviceInventoryService.PartnerAssignedDevice(partnerId));
		}

		@GetMapping("/self/care/devices")
		public ResponseEntity<List<ProductResponse>> aSelfcareDevices() {
			return ResponseEntity.ok(deviceInventoryService.SelfCareDevices());
		}

		//this end point service merge with Sim 	
		//		@GetMapping("/device/details/partner/{partnerId}")
		//		public ResponseEntity<Map<String, Integer>> getPartnerDeviceDetails(@PathVariable int partnerId){
		//			return ResponseEntity.ok(deviceInventoryService.totalDeviceInfoPartner(partnerId));
		//		}

		@PostMapping("/save_device")
		public ResponseEntity<?> addDeviceInformation(@RequestParam("image") MultipartFile image,
				@RequestParam("deviceData") String deviceData) throws IOException{

			logger.info("image information : {} ", image.getOriginalFilename());
			logger.info("device : {}", deviceData);
			
			DeviceInventoryDto deviceInventoryDto = null;
			try {
				
				deviceInventoryDto = mapper.readValue(deviceData, DeviceInventoryDto.class);
				
			} catch (JsonProcessingException e) {
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request !!");
			} 

			deviceInventoryService.saveDeviceInfo(deviceInventoryDto, image);
			
			return ResponseEntity.ok("done");

		}
}
