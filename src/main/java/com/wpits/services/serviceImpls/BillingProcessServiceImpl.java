package com.wpits.services.serviceImpls;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.BillingProcessDto;
import com.wpits.entities.BillingProcess;
import com.wpits.entities.Entitys;
import com.wpits.entities.PaperInvoiceBatch;
import com.wpits.entities.PeriodUnit;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.BillingProcessRepository;
import com.wpits.repositories.EntitysRepository;
import com.wpits.repositories.PaperInvoiceBatchRepository;
import com.wpits.repositories.PeriodUnitRepository;
import com.wpits.services.BillingProcessService;
@Service
public class BillingProcessServiceImpl implements BillingProcessService {

	@Autowired
	private BillingProcessRepository billingProcessRepository;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private PeriodUnitRepository periodUnitRepository;
	@Autowired
	private EntitysRepository entitysRepository;
	@Autowired
	private PaperInvoiceBatchRepository paperInvoiceBatchRepository;
	
	@Override
	public BillingProcessDto createBillingProcess(BillingProcessDto billingProcessDto,int periodUnitId,int entityId,int paperInvoiceBatchId) {
		PeriodUnit periodUnit = periodUnitRepository.findById(periodUnitId).orElseThrow( () -> new ResourceNotFoundException("period unit not found with given id !!"));
		Entitys entitys = entitysRepository.findById(entityId).orElseThrow( () -> new ResourceNotFoundException("entity not found with given id !!"));
		PaperInvoiceBatch paperInvoiceBatch = paperInvoiceBatchRepository.findById(paperInvoiceBatchId).orElseThrow( () -> new ResourceNotFoundException("paper invoice batch not found with given id !!"));
		BillingProcess billingProcess = mapper.map(billingProcessDto, BillingProcess.class);
		billingProcess.setPeriodUnit(periodUnit);
		billingProcess.setEntitys(entitys);
		billingProcess.setPaperInvoiceBatch(paperInvoiceBatch);
		billingProcess.setBillingDate(LocalDate.now());
		billingProcess.setRetriesToDo(0);
		billingProcess.setOptlock(new Random().nextInt(999999));		
		return mapper.map(billingProcessRepository.save(billingProcess), BillingProcessDto.class);
	}

	@Override
	public BillingProcessDto updateBillingProcess(BillingProcessDto billingProcessDto, int billingProcessId) {
		BillingProcess billingProcess = billingProcessRepository.findById(billingProcessId).orElseThrow( () -> new ResourceNotFoundException("billing process not found with given id !!"));
		billingProcess.setBillingDate(LocalDate.now());
		billingProcess.setPeriodValue(billingProcessDto.getPeriodValue());
		billingProcess.setIsReview(billingProcessDto.getIsReview());
		billingProcess.setRetriesToDo(0);
		billingProcess.setOptlock(billingProcessDto.getOptlock());
		return mapper.map(billingProcessRepository.save(billingProcess), BillingProcessDto.class);
	}

	@Override
	public List<BillingProcessDto> getAllBillingProcess() {
		return billingProcessRepository.findAll().stream().map(billingProcess -> mapper.map(billingProcess, BillingProcessDto.class)).collect(Collectors.toList());
	}

	@Override
	public void deletBillingProcess(int billingProcessId) {
		BillingProcess billingProcess = billingProcessRepository.findById(billingProcessId).orElseThrow( () -> new ResourceNotFoundException("billing process not found with given id !!"));
		billingProcessRepository.delete(billingProcess);
	}

}
