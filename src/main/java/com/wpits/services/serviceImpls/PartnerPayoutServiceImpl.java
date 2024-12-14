package com.wpits.services.serviceImpls;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.PartnerPayoutDto;
import com.wpits.entities.Partner;
import com.wpits.entities.PartnerPayout;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.PartnerPayoutRepository;
import com.wpits.repositories.PartnerRepository;
import com.wpits.services.PartnerPayoutService;

@Service
public class PartnerPayoutServiceImpl implements PartnerPayoutService {
	
	@Autowired
	private PartnerPayoutRepository partnerPayoutRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public PartnerPayoutDto createPartnerPayout(PartnerPayoutDto partnerPayoutDto, int partnerId) {
		Partner partner = partnerRepository.findById(partnerId).orElseThrow( () -> new ResourceNotFoundException("partner not found with given id !!"));
		PartnerPayout partnerPayout = mapper.map(partnerPayoutDto, PartnerPayout.class);
		partnerPayout.setPartner(partner);
		partnerPayout.setOptlock(new Random().nextInt(999999));
		return mapper.map(partnerPayoutRepository.save(partnerPayout), PartnerPayoutDto.class);
	}

	@Override
	public PartnerPayoutDto updatePartnerPayoutDto(PartnerPayoutDto partnerPayoutDto, int PartnerPayoutId) {
		PartnerPayout partnerPayout = partnerPayoutRepository.findById(PartnerPayoutId).orElseThrow( () -> new ResourceNotFoundException("partnerPayout not found with given id !!"));
		partnerPayout.setStartingDate(partnerPayoutDto.getStartingDate());
		partnerPayout.setEndingDate(partnerPayoutDto.getEndingDate());
		partnerPayout.setPaymentsAmount(partnerPayoutDto.getPaymentsAmount());
		partnerPayout.setRefundsAmount(partnerPayoutDto.getRefundsAmount());
		partnerPayout.setBalanceLeft(partnerPayoutDto.getBalanceLeft());
		partnerPayout.setPaymentId(partnerPayoutDto.getPaymentId());
		partnerPayout.setOptlock(partnerPayoutDto.getOptlock());
		return mapper.map(partnerPayoutRepository.save(partnerPayout), PartnerPayoutDto.class);
	}

	@Override
	public List<PartnerPayoutDto> getAllPartnerPayouts() {
		return partnerPayoutRepository.findAll().stream().map( partnerPayout -> mapper.map(partnerPayout, PartnerPayoutDto.class)).collect(Collectors.toList());
	}

	@Override
	public void deletePartnerPayout(int PartnerPayoutId) {
		PartnerPayout partnerPayout = partnerPayoutRepository.findById(PartnerPayoutId).orElseThrow( () -> new ResourceNotFoundException("partnerPayout not found with given id !!"));
		partnerPayoutRepository.delete(partnerPayout);
	}

}
