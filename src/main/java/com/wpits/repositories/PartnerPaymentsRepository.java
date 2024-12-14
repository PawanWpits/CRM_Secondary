package com.wpits.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wpits.entities.PartnerPayment;
import com.wpits.entities.PartnerPayment.Status;

public interface PartnerPaymentsRepository extends JpaRepository<PartnerPayment, Integer>{

	//Optional<PartnerPayment> findByPaymentToken(String paymentToken);

	//void updateStatus(Status used);
}
