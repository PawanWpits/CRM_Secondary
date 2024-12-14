package com.wpits.services;

import java.util.List;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.CartDto;

public interface CartService {
	
	ApiResponseMessage createCart(String token,int deviceID, int partnerId, int routerSerialNo);
	
	List<CartDto> getCart(String token);
	
	void deleteCart(int cartId); 
	
	void clearCartByExpireDate(); //Schedular

}
