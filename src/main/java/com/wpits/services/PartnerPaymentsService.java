package com.wpits.services;

import java.util.List;

import com.wpits.dtos.PartnerPaymentsDto;

public interface PartnerPaymentsService {

	PartnerPaymentsDto createPayment(PartnerPaymentsDto partnerProductPaymentsDto, int currencyId, int creditCard, String startingNumber, String endingNumber);
	
	List<PartnerPaymentsDto> getPayments();
}
