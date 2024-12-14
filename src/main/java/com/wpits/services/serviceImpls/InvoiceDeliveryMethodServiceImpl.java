package com.wpits.services.serviceImpls;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.InvoiceDeliveryMethodDto;
import com.wpits.entities.InvoiceDeliveryMethod;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.InvoiceDeliveryMethodRepository;
import com.wpits.services.InvoiceDeliveryMethodService;

@Service
public class InvoiceDeliveryMethodServiceImpl implements InvoiceDeliveryMethodService{

	@Autowired
	private InvoiceDeliveryMethodRepository invoiceDeleveryRepository;
	@Autowired
	private ModelMapper mapper;
	
	
	@Override
	public InvoiceDeliveryMethodDto createInvoiceDelevery(InvoiceDeliveryMethodDto invoiceDeliveryMethodDto) {
		
		InvoiceDeliveryMethod invoiceDeliveryMethod = mapper.map(invoiceDeliveryMethodDto, InvoiceDeliveryMethod.class);
		InvoiceDeliveryMethod savedInvoiceDelevery = invoiceDeleveryRepository.save(invoiceDeliveryMethod);
		return mapper.map(savedInvoiceDelevery, InvoiceDeliveryMethodDto.class);
	}

	@Override
	public List<InvoiceDeliveryMethodDto> getAllInvoiceDelevery() {
		
		List<InvoiceDeliveryMethod> invoices = invoiceDeleveryRepository.findAll();
		List<InvoiceDeliveryMethodDto> invoiceDtos = invoices.stream().map(invoice -> mapper.map(invoice, InvoiceDeliveryMethodDto.class)).collect(Collectors.toList());
		
		return invoiceDtos;
	}

	@Override
	public void deleteInvoiceDelevery(int invoiceId) {
		
		InvoiceDeliveryMethod invoiceDelevery = invoiceDeleveryRepository.findById(invoiceId).orElseThrow( () -> new ResourceNotFoundException("invoice delevery not found with given id !!"));
		invoiceDeleveryRepository.delete(invoiceDelevery);
	}

}
