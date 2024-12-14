package com.wpits.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.OrderResponseDTO;
import com.wpits.services.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {

	
	private final OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	
	@PostMapping("/save/order/token/{token}")
	public ResponseEntity<OrderResponseDTO> createOrder(@PathVariable String token){
		return new ResponseEntity<OrderResponseDTO>(orderService.createOrder(token),HttpStatus.CREATED);
	}
	
//	@GetMapping("/orders")
//	public ResponseEntity<List<OrderResponseDTO>> orders(){
//		return ResponseEntity.ok(orderService.orders());
//	}
//	
//	@GetMapping("/order/ordernumber/{orderNumber}")
//	public ResponseEntity<OrderResponseDTO> orderByOrderNumber(@PathVariable String orderNumber){
//		return ResponseEntity.ok(orderService.findByOrderNumber(orderNumber));
//	}
}
