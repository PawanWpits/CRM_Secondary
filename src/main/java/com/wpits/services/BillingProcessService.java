package com.wpits.services;

import java.util.List;

import com.wpits.dtos.BillingProcessDto;

public interface BillingProcessService {

	BillingProcessDto createBillingProcess(BillingProcessDto billingProcessDto,int periodUnitId,int entityId,int paperInvoiceBatchId);
	BillingProcessDto updateBillingProcess(BillingProcessDto billingProcessDto,int billingProcessId);
	List<BillingProcessDto> getAllBillingProcess();
	void deletBillingProcess(int billingProcessId);
}
