package com.wpits.services;

import com.wpits.dtos.OrderResponseDTO;

public interface OrderService {

	OrderResponseDTO createOrder(String token);
	
	//List<OrderResponseDTO> orders();
	
	//OrderResponseDTO findByOrderNumber(String orderNumber);
}
