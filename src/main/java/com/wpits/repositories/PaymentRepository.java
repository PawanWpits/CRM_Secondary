package com.wpits.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{

	Optional<Payment> findByCustomerId(int custId);

}
