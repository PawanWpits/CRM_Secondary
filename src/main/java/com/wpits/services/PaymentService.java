package com.wpits.services;

import java.util.List;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.PaymentDto;

public interface PaymentService {
	
	ApiResponseMessage createPayment(PaymentDto paymentDto, int currencyId, int paymentResultId, int paymentMethodId, int creditCardId, String msisdn);
		
	PaymentDto updatepayment(PaymentDto paymentDto, int paymentId);
	
	List<PaymentDto> getAllPayment();
	
	PaymentDto getByIdPayment(int paymentId);
	
	void deletePayment(int paymentId);
	
	PaymentDto getPaymentRecordByCustId(int custId);
	
	ApiResponseMessage postPaidBillPayment(String invoiceNumber, Double amount, int currencyId, int paymentResultId, int paymentMethodId,int creditCardId,int partnerId, String transactionId);
	
	void updatePostpaidAmountWithLateCharge();
}
