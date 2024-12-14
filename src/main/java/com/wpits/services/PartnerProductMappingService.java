package com.wpits.services;

import java.util.List;

import com.wpits.dtos.PartnerProductMappingDto;

public interface PartnerProductMappingService {

	PartnerProductMappingDto createPartnerProductMapping(PartnerProductMappingDto partnerProductMappingDto, int partnerId);
	
	PartnerProductMappingDto updatePartnerProductMapping(PartnerProductMappingDto partnerProductMappingDto, int partnerProductMappingId);
	
	List<PartnerProductMappingDto> getAllpartnerProductMapping();
	
	PartnerProductMappingDto getByIdpartnerProductMapping(int partnerProductMappingId);
	
	void deletepartnerProductMapping(int partnerProductMappingId);
}
