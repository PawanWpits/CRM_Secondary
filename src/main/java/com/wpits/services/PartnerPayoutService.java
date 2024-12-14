package com.wpits.services;

import java.util.List;

import com.wpits.dtos.PartnerPayoutDto;

public interface PartnerPayoutService {
	
	PartnerPayoutDto createPartnerPayout(PartnerPayoutDto partnerPayoutDto, int partnerId);
	
	PartnerPayoutDto updatePartnerPayoutDto(PartnerPayoutDto partnerPayoutDto, int PartnerPayoutId);
	
	List<PartnerPayoutDto> getAllPartnerPayouts();
	
	void deletePartnerPayout(int PartnerPayoutId);

}
