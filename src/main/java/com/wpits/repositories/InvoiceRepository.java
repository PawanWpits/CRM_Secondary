package com.wpits.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.Currency;
import com.wpits.entities.Invoice;
import com.wpits.entities.Invoice.Status;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{

	List<Invoice> findByDueDateBeforeAndStatus(LocalDate date, Status status);

	List<Invoice> findByMsisdnOrderByIdDesc(String msisdn);
	
	Optional<Invoice> findByMsisdnAndStatus(String msisdn,Status status);

	List<Invoice> findByStatusAndIsVip(Status status, boolean isVip);

	Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
