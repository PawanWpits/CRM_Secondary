package com.wpits.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.PartnerInvoice;

public interface PartnerInvoiceRepository extends JpaRepository<PartnerInvoice, Integer>{

	Optional<PartnerInvoice> findByInvoiceNumber(String invoiceNumber);

}
