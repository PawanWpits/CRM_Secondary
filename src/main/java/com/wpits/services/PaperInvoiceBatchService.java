package com.wpits.services;

import java.util.List;

import com.wpits.dtos.PaperInvoiceBatchDto;

public interface PaperInvoiceBatchService {

	PaperInvoiceBatchDto createpaperInvoiceBatch(PaperInvoiceBatchDto paperInvoiceBatchDto);
	
	PaperInvoiceBatchDto updatepaperInvoiceBatch(PaperInvoiceBatchDto paperInvoiceBatchDto,int paperInvoceId);
	
	List<PaperInvoiceBatchDto> getAllPaperInvoiceBatch();
	
	void deletePaperInvoiceBatch(int paperInvoceId);
	
}
