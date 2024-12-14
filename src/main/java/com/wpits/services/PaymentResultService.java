package com.wpits.services;

import java.util.List;

import com.wpits.dtos.PaymentResultDto;

public interface PaymentResultService {

	PaymentResultDto createPaymentResult(PaymentResultDto paymentResultDto);
	
	List<PaymentResultDto> getAllPaymentResult();
	
	void deletePaymentResult(int paymentResultId);
}
