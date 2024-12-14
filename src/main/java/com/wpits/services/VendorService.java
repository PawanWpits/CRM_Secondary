package com.wpits.services;

import java.util.List;

import com.wpits.dtos.VendorDto;

public interface VendorService {

	VendorDto createVendor(VendorDto vendorDto);
	
	VendorDto updateVendor(int vendorId,VendorDto vendorDto);
	
	List<VendorDto> getVendors();
	
	VendorDto getByIdVendor(int vendorId);
	
	void deleteVendor(int vendorId);
	
	
}
