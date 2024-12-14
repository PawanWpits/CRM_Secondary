package com.wpits.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.PartnerOrderRequest;
import com.wpits.services.PartnerOrderService;

@RestController
@RequestMapping("/api")
public class PartnerOrderController {

	@Autowired
	private PartnerOrderService partnerOrderService;
	
	
	@PostMapping("/partner/order")
	public ResponseEntity<Map<String, Object>> createOrder(@RequestBody PartnerOrderRequest orderRequest){
		return ResponseEntity.ok(partnerOrderService.createPartnerOrder(orderRequest));
	}
}