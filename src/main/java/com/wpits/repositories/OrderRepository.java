package com.wpits.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	Optional<Order> findByOrderNumber(String orderNumber);

	Order findByTokenAndPaymentStatusIsFalse(String token);

}
