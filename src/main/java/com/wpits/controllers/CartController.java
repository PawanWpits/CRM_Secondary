package com.wpits.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.CartDto;
import com.wpits.services.CartService;

@RestController
@RequestMapping("/api")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@PostMapping("/save/cart/token/{token}")
	public ResponseEntity<ApiResponseMessage> saveToCart(@PathVariable String token,
			@RequestParam(value = "partner", defaultValue = "0") int partnerId, 
			@RequestParam(value = "device", defaultValue = "0") int deviceId,
			@RequestParam(value = "router",defaultValue = "0") int routerSerialNo) {
		//cartService.createCart(token, deviceID, partnerId);
		return ResponseEntity.ok(cartService.createCart(token, deviceId, partnerId, routerSerialNo));

	}

	@GetMapping("/carts/token/{token}")
	public ResponseEntity<List<CartDto>> getAllCart(@PathVariable String token){
		return ResponseEntity.ok(cartService.getCart(token));
	}
	
	@DeleteMapping("/cart/id/{cartId}")
	public ResponseEntity<ApiResponseMessage> deleteCart(@PathVariable int cartId){
		cartService.deleteCart(cartId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
}
