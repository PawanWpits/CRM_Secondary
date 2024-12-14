package com.wpits.services.serviceImpls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.PartnerCommissionDto;
import com.wpits.entities.Currency;
import com.wpits.entities.Partner;
import com.wpits.entities.PartnerCommission;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.CurrencyRepository;
import com.wpits.repositories.PartnerCommissionRepository;
import com.wpits.repositories.PartnerRepository;
import com.wpits.services.PartnerCommissionService;

@Service
public class PartnerCommissionServiceImpl implements PartnerCommissionService{
	
	@Autowired
	private PartnerCommissionRepository partnerCommissionRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;

	@Override
	public PartnerCommissionDto createPartnerCommission(PartnerCommissionDto partnerCommissionDto, int currencyId) {
		Partner partner = partnerRepository.findById(partnerCommissionDto.getPartnerId()).orElseThrow( () -> new ResourceNotFoundException("partner not found with given id !!"));
		Currency currency = currencyRepository.findById(currencyId).orElseThrow( () -> new ResourceNotFoundException("currency not found with given id !!"));
		PartnerCommission partnerCommission = mapper.map(partnerCommissionDto, PartnerCommission.class);
		partnerCommission.setCurrency(currency);
		//product segregation based on Quantity
		
		return mapper.map(partnerCommissionRepository.save(partnerCommission), PartnerCommissionDto.class);
	}

	@Override
	public PartnerCommissionDto updatePartnerCommission(PartnerCommissionDto partnerCommissionDto,int partnerCommissionId) {
		PartnerCommission partnerCommission = partnerCommissionRepository.findById(partnerCommissionId).orElseThrow( () -> new ResourceNotFoundException("partner commission not found with given id !!"));
		partnerCommission.setAmount(partnerCommissionDto.getAmount());
		partnerCommission.setType(partnerCommissionDto.getType());
		partnerCommission.setPartnerId(partnerCommissionDto.getPartnerId());
		partnerCommission.setCommissionProcessRunId(partnerCommissionDto.getCommissionProcessRunId());
		return mapper.map(partnerCommissionRepository.save(partnerCommission), PartnerCommissionDto.class);
	}

	@Override
	public List<PartnerCommissionDto> getAllPartnerCommission() {
		return partnerCommissionRepository.findAll().stream().map( partnerCommission -> mapper.map(partnerCommission, PartnerCommissionDto.class)).collect(Collectors.toList());
	}

	@Override
	public PartnerCommissionDto findByIdPartnerCommission(int partnerCommissionId) {
		PartnerCommission partnerCommission = partnerCommissionRepository.findById(partnerCommissionId).orElseThrow( () -> new ResourceNotFoundException("partner commission not found with given id !!"));
		return mapper.map(partnerCommission, PartnerCommissionDto.class);
	}
	
	@Override
	public void deletePartnerCommision(int partnerCommissionId) {
		PartnerCommission partnerCommission = partnerCommissionRepository.findById(partnerCommissionId).orElseThrow( () -> new ResourceNotFoundException("partner commission not found with given id !!"));
		partnerCommissionRepository.delete(partnerCommission);
	}

	@Override
	public List<Map<String, Object>> getCommissionByPartnerId(int partnerId) {
		
		List<Map<String, Object>> commin = new ArrayList<>();
		
		Partner partner = partnerRepository.findById(partnerId).orElseThrow( () -> new ResourceNotFoundException("partner not found with given id !!"));
		
		List<PartnerCommission> partnerCommissions = partnerCommissionRepository.findByPartnerId(partner.getId()).orElseThrow( () -> new ResourceNotFoundException("commission not found with given partner id !!"));
		
		for (PartnerCommission partnerCommission : partnerCommissions) {
			Map<String, Object> commission = new HashMap<>();
			commission.put("Agent Id", partnerCommission.getPartnerId());
			commission.put("Agent Name", partner.getFristName()+" "+partner.getLastName());
			commission.put("Currency", partnerCommission.getCurrency().getSymbol());
			commission.put("Amount", partnerCommission.getAmount());
			commission.put("Type", partnerCommission.getType());
			commission.put("Commission Process Run Id", partnerCommission.getCommissionProcessRunId());
			
			commin.add(commission);
		}
		
		
		//System.out.println(commission);
		return commin;
	}
}
