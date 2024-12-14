package com.wpits.services.serviceImpls;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.PartnerDto;
import com.wpits.entities.BaseUser;
import com.wpits.entities.Partner;
import com.wpits.entities.PartnerCommission;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.BaseUserRepository;
import com.wpits.repositories.PartnerCommissionRepository;
import com.wpits.repositories.PartnerRepository;
import com.wpits.services.PartnerService;

@Service
public class PartnerServiceImpl implements PartnerService {
	
	@Autowired
	private PartnerRepository partnerRepository;
	@Autowired
	private BaseUserRepository baseUserRepository;
	@Autowired
	private PartnerCommissionRepository partnerCommissionRepository;
	@Autowired
	private ModelMapper mapper;

	@Override
	public PartnerDto createPartner(PartnerDto partnerDto, int baseUserId, int partnerCommissionId) {
		
		BaseUser baseUser = baseUserRepository.findById(baseUserId).orElseThrow( () -> new ResourceNotFoundException("base user not found with given id"));
		PartnerCommission partnerCommission = partnerCommissionRepository.findById(partnerCommissionId).orElseThrow( () -> new ResourceNotFoundException("partner commission not found with given id !!"));
		Partner partner = mapper.map(partnerDto, Partner.class);
		partner.setBaseUser(baseUser);
		partner.setPartnerCommission(partnerCommission);
		partner.setOptlock(new Random().nextInt(999999));
		partner.setCreationDate(LocalDate.now());
		Partner savedPartner = partnerRepository.save(partner);
		return mapper.map(savedPartner, PartnerDto.class);
	}

	@Override
	public PartnerDto updatePartner(PartnerDto partnerDto, int partnerId) {
		
		Partner partner = partnerRepository.findById(partnerId).orElseThrow( () -> new ResourceNotFoundException("partner not found with given id !!"));
		partner.setTotalPayments(partnerDto.getTotalPayments());
		partner.setTotalRefunds(partnerDto.getTotalRefunds());
		partner.setTotalPayouts(partnerDto.getTotalPayouts());
		partner.setDuePayout(partnerDto.getDuePayout());
		partner.setOptlock(partnerDto.getOptlock());
		partner.setType(partnerDto.getType());
		partner.setParentId(partnerDto.getParentId());
		partner.setCommissionType(partnerDto.getCommissionType());
		partner.setFristName(partnerDto.getFristName());
		partner.setLastName(partnerDto.getLastName());
		partner.setEmail(partnerDto.getEmail());
		partner.setBusinessAddress(partnerDto.getBusinessAddress());
		partner.setBusinessNature(partnerDto.getBusinessNature());
		partner.setContact(partnerDto.getContact());
		partner.setDocumentId(partnerDto.getDocumentId());
		partner.setDocumentType(partnerDto.getDocumentType());
		partner.setToken(partnerDto.getToken());
		partner.setCreationDate(LocalDate.now());
		partner.setLocallity(partnerDto.getLocallity());
		partner.setCoordinate(partnerDto.getCoordinate());
		partner.setReasonStatus(partnerDto.getReasonStatus());
		partner.setIsActive(partnerDto.getIsActive());	
		Partner savedPartner = partnerRepository.save(partner);
		return mapper.map(savedPartner, PartnerDto.class);
	}

	@Override
	public List<PartnerDto> getAllPartner() {
		return partnerRepository.findAll().stream().map(partner -> mapper.map(partner, PartnerDto.class)).collect(Collectors.toList());
	}
	
	@Override
	public PartnerDto findByIdPartner(int partnerId) {
		Partner partner = partnerRepository.findById(partnerId).orElseThrow( () -> new ResourceNotFoundException("partner not found with given id !!"));
		return mapper.map(partner, PartnerDto.class);
	}



	@Override
	public void deletePartner(int partnerId) {
		Partner partner = partnerRepository.findById(partnerId).orElseThrow( () -> new ResourceNotFoundException("partner not found with given id !!"));
		partnerRepository.delete(partner);
	}

	@Override
	public PartnerDto findByPartnerContact(String contact) {
		Partner partner = partnerRepository.findByContact(contact).orElseThrow( () -> new ResourceNotFoundException("partner not found with given number !!"));
		return mapper.map(partner, PartnerDto.class);
	}

}
