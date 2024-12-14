package com.wpits.services.serviceImpls;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.VendorDto;
import com.wpits.entities.BaseUser;
import com.wpits.entities.Vendor;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.BaseUserRepository;
import com.wpits.repositories.VendorRepository;
import com.wpits.services.VendorService;

@Service
public class VendorServiceImpl implements VendorService {
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private BaseUserRepository baseUserRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public VendorDto createVendor(VendorDto vendorDto) {
		BaseUser baseUser = baseUserRepository.findById(vendorDto.getUserId()).orElseThrow( () -> new ResourceNotFoundException("base user not found with given id !!"));
		Vendor vendor = mapper.map(vendorDto, Vendor.class);
		vendor.setToken(new Random().nextInt(99999999)+"");
		vendor.setCreateDate(LocalDate.now());
		Vendor savedVendor = vendorRepository.save(vendor);
		return mapper.map(savedVendor, VendorDto.class);
	}

	@Override
	public VendorDto updateVendor(int vendorId, VendorDto vendorDto) {
		Vendor vendor = vendorRepository.findById(vendorId).orElseThrow( () -> new ResourceNotFoundException("vendor not found with given id !!"));
		vendor.setFirstName(vendorDto.getFirstName());
		vendor.setMaidenName(vendorDto.getMaidenName());
		vendor.setLastName(vendorDto.getLastName());
		vendor.setEmail(vendorDto.getEmail());
		vendor.setContact(vendorDto.getContact());
		vendor.setAddress(vendorDto.getAddress());
		vendor.setCompanyName(vendorDto.getCompanyName());
		vendor.setCreateDate(vendor.getCreateDate());
		vendor.setToken(vendor.getToken());
		vendor.setUserId(vendor.getUserId());
		Vendor savedVendor = vendorRepository.save(vendor);
		return mapper.map(savedVendor, VendorDto.class);
	}

	@Override
	public List<VendorDto> getVendors() {
		// TODO Auto-generated method stub
		return vendorRepository.findAll().stream().map( vendor -> mapper.map(vendor, VendorDto.class)).collect(Collectors.toList());
	}

	@Override
	public VendorDto getByIdVendor(int vendorId) {
		Vendor vendor = vendorRepository.findById(vendorId).orElseThrow( () -> new ResourceNotFoundException("vendor not found with given id !!"));
		return mapper.map(vendor, VendorDto.class);
	}

	@Override
	public void deleteVendor(int vendorId) {
		Vendor vendor = vendorRepository.findById(vendorId).orElseThrow( () -> new ResourceNotFoundException("vendor not found with given id !!"));
		vendorRepository.delete(vendor);
	}

}
