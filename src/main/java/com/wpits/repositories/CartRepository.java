package com.wpits.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{

	List<Cart> findByToken(String token);
	
	List<Cart> findByDeviceId(int deviceId);
	
	List<Cart> findByExpiryDate(LocalDate expiryDate);

	List<Cart> findByRouterSerialNo(int routerSerialNo);
}
