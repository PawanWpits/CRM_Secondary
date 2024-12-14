package com.wpits.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wpits.dtos.PartnerSimResponse;
import com.wpits.dtos.RouterInventoryDto;
import com.wpits.helpers.ExcelHelper;
import com.wpits.services.RouterInventoryService;

@RestController
@RequestMapping("/api")
public class RouterInventoryController {

	private final RouterInventoryService routerInventoryService;

	public RouterInventoryController(RouterInventoryService routerInventoryService) {
		this.routerInventoryService = routerInventoryService;
	}
	
	@PostMapping("/save/router_device")
	public ResponseEntity<RouterInventoryDto> saveRouterInventory(@RequestBody RouterInventoryDto routerInventoryDto){
		return new ResponseEntity<>(routerInventoryService.createRouterInventory(routerInventoryDto),HttpStatus.CREATED);
	}
	
	
	//upload router by excel vendor
	@PostMapping("/router/upload/excel")
	public ResponseEntity<?> uploadExcel(@RequestParam("file")MultipartFile file,@RequestParam int vendorId) throws IOException{
		
		if (ExcelHelper.checkExcelFormat(file)) {			
			
			routerInventoryService.saveExcel(file, vendorId);
			
			return ResponseEntity.ok(Map.of("message","file uploaded successfully !!"));
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please upload excel file !!");
	}

	@GetMapping("/routers")
	public ResponseEntity<List<RouterInventoryDto>> getAllRouters() {
		return ResponseEntity.ok(routerInventoryService.getAllRouters());
	}

	//assigned Router of partner
	@GetMapping("/assigned/router/partner/{partnerId}")
	public ResponseEntity<?> assignedSimOfPartner(@PathVariable int partnerId) {
		return ResponseEntity.ok(routerInventoryService.partnerAssignedRouters(partnerId));
	}
	
	@PatchMapping("/router/mac_address/{macAddress}/password/{password}")
	public ResponseEntity<?> changeRouterPassword(@PathVariable String macAddress,@PathVariable String password){
		return ResponseEntity.ok(routerInventoryService.changePassword(macAddress, password));
	}
	
	@GetMapping("/customer/detail/by/mac_address/{macAddress}")
	public ResponseEntity<?> assignedSimOfPartner(@PathVariable String macAddress) {
		return ResponseEntity.ok(routerInventoryService.getCustomerDetailsOfRouter(macAddress));
	}
}
