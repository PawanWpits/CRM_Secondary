package com.wpits.services.serviceImpls;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.PaperInvoiceBatchDto;
import com.wpits.entities.PaperInvoiceBatch;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.PaperInvoiceBatchRepository;
import com.wpits.services.PaperInvoiceBatchService;

@Service
public class PaperInvoiceBatchServiceImpl implements PaperInvoiceBatchService {
	
	@Autowired
	private PaperInvoiceBatchRepository paperInvoiceBatchRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public PaperInvoiceBatchDto createpaperInvoiceBatch(PaperInvoiceBatchDto paperInvoiceBatchDto) {
		PaperInvoiceBatch paperInvoiceBatch = mapper.map(paperInvoiceBatchDto, PaperInvoiceBatch.class);
		paperInvoiceBatch.setDeliveryDate(LocalDate.now());
		paperInvoiceBatch.setOptlock(new Random().nextInt(999999));		
		return mapper.map(paperInvoiceBatchRepository.save(paperInvoiceBatch), PaperInvoiceBatchDto.class);
	}

	@Override
	public PaperInvoiceBatchDto updatepaperInvoiceBatch(PaperInvoiceBatchDto paperInvoiceBatchDto, int paperInvoceId) {
		PaperInvoiceBatch paperInvoiceBatch = paperInvoiceBatchRepository.findById(paperInvoceId).orElseThrow( () -> new ResourceNotFoundException("paper invice batch not found with given id !!"));
		paperInvoiceBatch.setTotalInvoices(paperInvoiceBatchDto.getTotalInvoices());
		paperInvoiceBatch.setDeliveryDate(LocalDate.now());
		paperInvoiceBatch.setIsSelfManaged(paperInvoiceBatchDto.getIsSelfManaged());
		paperInvoiceBatch.setOptlock(paperInvoiceBatchDto.getOptlock());
		return mapper.map(paperInvoiceBatchRepository.save(paperInvoiceBatch), PaperInvoiceBatchDto.class);
	}

	@Override
	public List<PaperInvoiceBatchDto> getAllPaperInvoiceBatch() {
		return paperInvoiceBatchRepository.findAll().stream().map( paperInvoice -> mapper.map(paperInvoice, PaperInvoiceBatchDto.class)).collect(Collectors.toList());
	}

	@Override
	public void deletePaperInvoiceBatch(int paperInvoceId) {
		PaperInvoiceBatch paperInvoiceBatch = paperInvoiceBatchRepository.findById(paperInvoceId).orElseThrow( () -> new ResourceNotFoundException("paper invice batch not found with given id !!"));
		paperInvoiceBatchRepository.delete(paperInvoiceBatch);
	}

}
