package com.wpits.services.serviceImpls;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wpits.dtos.DeviceInventoryDto;
import com.wpits.dtos.ProductResponse;
import com.wpits.entities.DeviceInventory;
import com.wpits.entities.Vendor;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.helpers.ExcelHelper;
import com.wpits.repositories.DeviceInventoryRepository;
import com.wpits.repositories.PartnerPaymentsRepository;
import com.wpits.repositories.VendorRepository;
import com.wpits.services.DeviceInventoryService;

@Service
public class DeviceInventoryServiceImpl implements DeviceInventoryService{
	
	@Autowired
	private DeviceInventoryRepository deviceInventoryRepository;
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PartnerPaymentsRepository partnerPaymentsRepository;

	@Override
	public DeviceInventoryDto createDeviceInventory(DeviceInventoryDto deviceInventoryDto) {
		
		Vendor vendor = vendorRepository.findById(deviceInventoryDto.getVendorId()).orElseThrow( () -> new ResourceNotFoundException("vendor not found with given id !!"));
		
		DeviceInventory deviceInventory = mapper.map(deviceInventoryDto, DeviceInventory.class);
		deviceInventory.setManufactureDate(LocalDate.now());
		deviceInventory.setVendorId(vendor.getId());
		
		return  mapper.map(deviceInventoryRepository.save(deviceInventory), DeviceInventoryDto.class);
	}

	@Override
	public DeviceInventoryDto updateDeviceInventory(DeviceInventoryDto deviceInventoryDto, int deviceInventoryId) {
		DeviceInventory deviceInventory = deviceInventoryRepository.findById(deviceInventoryId).orElseThrow( () -> new ResourceNotFoundException("device inventory not found with the given id !!"));
		deviceInventory.setDeviceModel(deviceInventoryDto.getDeviceModel());
		deviceInventory.setImi(deviceInventory.getImi());
		deviceInventory.setVendorId(deviceInventory.getVendorId());
		deviceInventory.setDeviceMake(deviceInventoryDto.getDeviceMake());
		deviceInventory.setConfiguration(deviceInventoryDto.getConfiguration());
		deviceInventory.setOstype(deviceInventoryDto.getOstype());
		deviceInventory.setManufacturer(deviceInventoryDto.getManufacturer());
		deviceInventory.setManufactureDate(LocalDate.now());
		deviceInventory.setBatchId(deviceInventoryDto.getBatchId());
		deviceInventory.setDeviceType(deviceInventoryDto.getDeviceType());
//		deviceInventory.setVendorName(deviceInventoryDto.getVendorName());
//		deviceInventory.setVendorContact(deviceInventoryDto.getVendorContact());
//		deviceInventory.setVendorAddress(deviceInventoryDto.getVendorAddress());
		deviceInventory.setBuyingPriceUsd(deviceInventoryDto.getBuyingPriceUsd());
		deviceInventory.setSellingPriceUsd(deviceInventoryDto.getSellingPriceUsd());
		deviceInventory.setVat(deviceInventoryDto.getVat());
		deviceInventory.setOtherTaxes(deviceInventoryDto.getOtherTaxes());
		deviceInventory.setMinCommision(deviceInventoryDto.getMinCommision());
		deviceInventory.setMaxCommision(deviceInventoryDto.getMaxCommision());
		deviceInventory.setAvgCommision(deviceInventoryDto.getAvgCommision());
		return  mapper.map(deviceInventoryRepository.save(deviceInventory), DeviceInventoryDto.class);
	}

	@Override
	public List<DeviceInventoryDto> getAllDeviceInventory() {
		return deviceInventoryRepository.findAll().stream().map( deviceInventory -> mapper.map(deviceInventory, DeviceInventoryDto.class)).collect(Collectors.toList());
	}

	@Override
	public DeviceInventoryDto getByIdDeviceInventory(int deviceInventoryId) {
		DeviceInventory deviceInventory = deviceInventoryRepository.findById(deviceInventoryId).orElseThrow( () -> new ResourceNotFoundException("device inventory not found with given id !!"));
		return mapper.map(deviceInventory, DeviceInventoryDto.class);
	}

	@Override
	public void deleteDeviceInventory(int deviceInventoryId) {
		DeviceInventory deviceInventory = deviceInventoryRepository.findById(deviceInventoryId).orElseThrow( () -> new ResourceNotFoundException("device inventory not found with given id !!"));
		deviceInventoryRepository.delete(deviceInventory);
	}

	@Override
	public void saveExcel(MultipartFile file, int vendorId) {
		
		Vendor vendor = vendorRepository.findById(vendorId).orElseThrow( () -> new ResourceNotFoundException("vendor not found with given id !!"));

		try {
			
			List<DeviceInventory> device = new ArrayList<>();
			
			List<DeviceInventory> deviceInventories = ExcelHelper.convertExcelToListOfDeviceInventory(file.getInputStream());
			
			System.out.println(deviceInventories.size());
			
			for (DeviceInventory excel : deviceInventories) {
				
				DeviceInventory inventory = new DeviceInventory();
				
				inventory.setDeviceModel(excel.getDeviceModel());
				inventory.setImi(excel.getImi());
				inventory.setVendorId(vendor.getId());
				inventory.setDeviceMake(excel.getDeviceMake());
				inventory.setConfiguration(excel.getConfiguration());
				inventory.setOstype(excel.getOstype());
				inventory.setManufacturer(excel.getManufacturer());
				inventory.setManufactureDate(LocalDate.now());
				inventory.setBatchId(excel.getBatchId());
				inventory.setDeviceType(excel.getDeviceType());
				inventory.setBuyingPriceUsd(excel.getBuyingPriceUsd());
				inventory.setSellingPriceUsd(excel.getSellingPriceUsd());
				inventory.setVat(excel.getVat());
				inventory.setOtherTaxes(excel.getOtherTaxes());
				inventory.setMinCommision(excel.getMinCommision());
				inventory.setMaxCommision(excel.getMaxCommision());
				inventory.setAvgCommision(excel.getAvgCommision());
				
				device.add(inventory);
				
			}

			 deviceInventoryRepository.saveAll(device);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	@Override
	public List<ProductResponse> PartnerAssignedDevice(int partnerId) {
	
		List<ProductResponse> response = new ArrayList<>();
		
		List<DeviceInventory> deviceList = deviceInventoryRepository.findByPartnerId(partnerId);
		
		for (DeviceInventory deviceInventory : deviceList) {
			
			ProductResponse deviceResponse = new ProductResponse();
			
			deviceResponse.setDeviceId(deviceInventory.getId());
			deviceResponse.setDeviceModel(deviceInventory.getDeviceModel());
			deviceResponse.setDeviceType(deviceInventory.getDeviceType());
			deviceResponse.setOsType(deviceInventory.getOstype());
			deviceResponse.setPrice(deviceInventory.getSellingPriceUsd());
			
			response.add(deviceResponse);
		}
		return response;
	}

	@Override
	public List<ProductResponse> SelfCareDevices() {
		
		List<ProductResponse> response = new ArrayList<>();
		
		List<DeviceInventory> devices = deviceInventoryRepository.findAll();
		
		for (DeviceInventory device : devices) {
			
			if (device.getPartnerId() == 0 && device.getAllocationDate() == null) {
				
				ProductResponse productResponse = new ProductResponse();
				
				productResponse.setDeviceId(device.getId());
				productResponse.setDeviceModel(device.getDeviceModel());
				productResponse.setDeviceType(device.getDeviceType());
				productResponse.setOsType(device.getOstype());
				productResponse.setPrice(device.getSellingPriceUsd());
				
				response.add(productResponse);
			}
				
			}
			return response;
	}

	@Override
	public void saveDeviceInfo(DeviceInventoryDto deviceInventoryDto, MultipartFile image) {
		
		System.out.println(deviceInventoryDto.getDeviceModel());
		System.out.println(image.getOriginalFilename());
	}

//	@Override
//	public Map<String, Integer> totalDeviceInfoPartner(int partnerId) {
//		
//		
//		
//		List<DeviceInventory> deviceList = deviceInventoryRepository.findByPartnerAllDevice(partnerId);
//		System.out.println("total device : "+deviceList.size());
//		
//		List<DeviceInventory> availableDevice = deviceList.stream().filter(available ->available.getAllocationDate() == null).collect(Collectors.toList());	
//		System.out.println("available device : "+availableDevice.size());
//		
//		List<DeviceInventory> allocatedDevice = deviceList.stream().filter(d -> d.getAllocationDate() != null ).collect(Collectors.toList());
//		System.out.println("allocated device : "+allocatedDevice.size());
//		
//		Map<String, Integer> response = new HashMap<>();
//		response.put("totalDevice", deviceList.size());
//		response.put("availableDevice", availableDevice.size());
//		response.put("allocatedDevice", allocatedDevice.size());
//		
//		
//		
//		return response;
//	}
	
	

}
