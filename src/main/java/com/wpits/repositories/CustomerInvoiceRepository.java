package com.wpits.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.CustomerInvoice;

public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Integer>{

	Optional<CustomerInvoice> findByOrderNumber(String orderNumber);
}