package com.wpits.services;

import java.util.List;
import java.util.Map;

import com.wpits.dtos.InvoiceDto;
import com.wpits.entities.Invoice;

public interface InvoiceService {

	InvoiceDto createInvoice(InvoiceDto invoiceDto,int billingProcessId,int paperInvoiceId,int currencyId);
	
	InvoiceDto updateInvoice(InvoiceDto invoiceDto,int invoiceId);
	
	List<InvoiceDto> getAllInvoice();
	
	InvoiceDto findByIdInvoice(int invoiceId);
	
	void deleteInvoice(int invoiceId);
	
	public void genrateInvoice();  //Schedular
	
	Map<String, Object> invoiceDetails();
	
	List<InvoiceDto> findInvoiceByMsisdn(String msisdn);
	
	InvoiceDto findUnpaidInviceByMsisdn(String msisdn);
}
