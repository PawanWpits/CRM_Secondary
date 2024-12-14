package com.wpits.services;

import java.util.List;

import com.wpits.dtos.InvoiceDeliveryMethodDto;

public interface InvoiceDeliveryMethodService {
	
	InvoiceDeliveryMethodDto createInvoiceDelevery(InvoiceDeliveryMethodDto invoiceDeliveryMethodDto);
	
	List<InvoiceDeliveryMethodDto> getAllInvoiceDelevery();
	
	void deleteInvoiceDelevery(int invoiceId);
	
	

}
