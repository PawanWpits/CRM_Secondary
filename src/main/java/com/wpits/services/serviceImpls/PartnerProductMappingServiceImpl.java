package com.wpits.services.serviceImpls;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.PartnerProductMappingDto;
import com.wpits.entities.Partner;
import com.wpits.entities.PartnerProductMapping;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.PartnerProductMappingRepository;
import com.wpits.repositories.PartnerRepository;
import com.wpits.services.PartnerProductMappingService;

@Service
public class PartnerProductMappingServiceImpl implements PartnerProductMappingService{
	
	@Autowired
	private PartnerProductMappingRepository partnerProductMappingRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public PartnerProductMappingDto createPartnerProductMapping(PartnerProductMappingDto partnerProductMappingDto, int partnerId) {
		Partner partner = partnerRepository.findById(partnerId).orElseThrow( () -> new ResourceNotFoundException("partner not found with given id !!"));
		PartnerProductMapping partnerProductMapping = mapper.map(partnerProductMappingDto, PartnerProductMapping.class);
		partnerProductMapping.setPartner(partner);
		return mapper.map(partnerProductMappingRepository.save(partnerProductMapping), PartnerProductMappingDto.class);
	}

	@Override
	public PartnerProductMappingDto updatePartnerProductMapping(PartnerProductMappingDto partnerProductMappingDto, int partnerProductMappingId) {
		PartnerProductMapping partnerProductMapping = partnerProductMappingRepository.findById(partnerProductMappingId).orElseThrow( () -> new ResourceNotFoundException("partner product mapping not found with given id !!"));
		partnerProductMapping.setProductId(partnerProductMappingDto.getProductId());
		partnerProductMapping.setCommissionType(partnerProductMappingDto.getCommissionType());
		partnerProductMapping.setCommissionValue(partnerProductMappingDto.getCommissionValue());
		return mapper.map(partnerProductMappingRepository.save(partnerProductMapping), PartnerProductMappingDto.class);
	}
 
	@Override
	public List<PartnerProductMappingDto> getAllpartnerProductMapping() {
		return partnerProductMappingRepository.findAll().stream().map( partnerProductMapping -> mapper.map(partnerProductMapping, PartnerProductMappingDto.class)).collect(Collectors.toList());
	}

	@Override
	public PartnerProductMappingDto getByIdpartnerProductMapping(int partnerProductMappingId) {
		PartnerProductMapping partnerProductMapping = partnerProductMappingRepository.findById(partnerProductMappingId).orElseThrow( () -> new ResourceNotFoundException("partner product mapping not found with given id !!"));
		return mapper.map(partnerProductMapping, PartnerProductMappingDto.class);
	}

	@Override
	public void deletepartnerProductMapping(int partnerProductMappingId) {
		PartnerProductMapping partnerProductMapping = partnerProductMappingRepository.findById(partnerProductMappingId).orElseThrow( () -> new ResourceNotFoundException("partner product mapping not found with given id !!"));
		partnerProductMappingRepository.delete(partnerProductMapping);
	}

}
