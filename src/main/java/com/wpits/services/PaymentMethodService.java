package com.wpits.services;

import java.util.List;

import com.wpits.dtos.PaymentMethodDto;

public interface PaymentMethodService {

	PaymentMethodDto createPaymentMethod(PaymentMethodDto paymentMethodDto);
	
	List<PaymentMethodDto> getALlPaymentMethod();
	
	void deletePaymentMethod(int paymentMethodId);
}
