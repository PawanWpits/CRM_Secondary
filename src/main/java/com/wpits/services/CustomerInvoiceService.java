package com.wpits.services;

import com.wpits.dtos.CustomerInvoiceDto;

public interface CustomerInvoiceService {
	
	//CustomerInvoiceDto createInvoice(int orderId);
	
	CustomerInvoiceDto findByOrderNumber(String orderNumber);
	
}
