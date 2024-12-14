package com.wpits.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer>{

}
