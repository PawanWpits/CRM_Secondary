package com.wpits.services.serviceImpls;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wpits.dtos.CustomerDto;
import com.wpits.dtos.RouterInventoryDto;
import com.wpits.entities.Customer;
import com.wpits.entities.RouterInventory;
import com.wpits.entities.Vendor;
import com.wpits.exceptions.BadApiRequestException;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.helpers.ExcelHelper;
import com.wpits.helpers.PasswordEncodeDecode;
import com.wpits.repositories.CustomerRepository;
import com.wpits.repositories.RouterInventoryRepository;
import com.wpits.repositories.VendorRepository;
import com.wpits.services.RouterInventoryService;

@Service
public class RouterInventoryServiceImpl implements RouterInventoryService{
	
	private final RouterInventoryRepository routerInventoryRepository;
	
	private final ModelMapper mapper;
	
	private final VendorRepository vendorRepository;
	
	private final CustomerRepository customerRepository;



	public RouterInventoryServiceImpl(RouterInventoryRepository routerInventoryRepository, ModelMapper mapper,
			VendorRepository vendorRepository, CustomerRepository customerRepository) {
		
		this.routerInventoryRepository = routerInventoryRepository;
		this.mapper = mapper;
		this.vendorRepository = vendorRepository;
		this.customerRepository = customerRepository;
	}


	private final Random random = new Random();

	@Override
	public RouterInventoryDto createRouterInventory(RouterInventoryDto routerInventoryDto) {
		
		RouterInventory inventory = mapper.map(routerInventoryDto, RouterInventory.class);
		inventory.setId(UUID.randomUUID().toString().replace("-", ""));
		inventory.setCreationTime(LocalDateTime.now());
		
		String password = null;
		try {
			password = PasswordEncodeDecode.encrypt(routerInventoryDto.getCpePassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
		inventory.setCpePassword(password);
		
		return mapper.map(routerInventoryRepository.save(inventory), RouterInventoryDto.class);
	}



	@Override
	public void saveExcel(MultipartFile file, int vendorId) throws IOException {

	    Vendor vendor = vendorRepository.findById(vendorId)
	            .orElseThrow(() -> new ResourceNotFoundException("vendor not found with given id !!"));

	    List<RouterInventory> routers = new ArrayList<>();

	    List<RouterInventory> routerInventories = ExcelHelper
	            .convertExcelToListOfRouterInventory(file.getInputStream());

	    for (RouterInventory excel : routerInventories) {

	        String accountNumber = UUID.randomUUID().toString().replace("-", "");
	        
	        RouterInventory inventory = new RouterInventory();

	        inventory.setId(accountNumber);
	        inventory.setCpeUsername(accountNumber); 

	        inventory.setSerialNumber(excel.getSerialNumber());
	        inventory.setType(excel.getType());
	        inventory.setBrand(excel.getBrand());
	        inventory.setSsId(random.nextInt(99999999));
	        inventory.setCpeConfigUrl(excel.getCpeConfigUrl());

	        String password = null;
	        try {
	            password = PasswordEncodeDecode.encrypt(random.nextInt(999999999) + "");
	        } catch (Exception e) {
	            throw new BadApiRequestException(e.getMessage());
	        }
	        inventory.setCpePassword(password);

	        inventory.setMacAddress(excel.getMacAddress());
	        inventory.setVendorId(vendor.getId());
	        inventory.setDeviceManufactorer(excel.getDeviceManufactorer());
	        inventory.setCreationTime(LocalDateTime.now());

	        routers.add(inventory);
	    }

	    routerInventoryRepository.saveAll(routers);
	}




	@Override
	public List<RouterInventoryDto> getAllRouters() {
		//return routerInventoryRepository.findAll().stream().map( router -> mapper.map(router, RouterInventoryDto.class)).collect(Collectors.toList());
		int partnerId=0;
		return routerInventoryRepository.findByPartnerIdAndActivationDateIsNullAndAllocationDateIsNull(partnerId).stream().map( router -> mapper.map(router, RouterInventoryDto.class)).collect(Collectors.toList());	
	}



	@Override
	public List<Map<String, Object>> partnerAssignedRouters(int partnerId) {
		
		List<RouterInventory> routers = routerInventoryRepository.findByPartnerIdAndActivationDateIsNullAndAllocationDateIsNull(partnerId);
		if (routers.isEmpty()) {
			throw new ResourceNotFoundException("partner your router bucket is empty !!");
		}
		
		//Map<String, Object> router = new HashMap<>();
		List<Map<String, Object>> routerResponse = new ArrayList<>();
		
		routers.forEach(router -> {
			Map<String, Object> routerRes = new HashMap<>();
			routerRes.put("AccountNumber", router.getId());
			routerRes.put("MacAddress", router.getMacAddress());
			routerRes.put("SerialNumber", router.getSerialNumber());
			routerRes.put("type", router.getType());
			//routerRes.put("UserName", router.getCpeUsername());
			routerRes.put("Password", router.getCpePassword());
			
			routerResponse.add(routerRes);
		});
		
		return routerResponse;
	}



	@Override
	public Map<String, Object> changePassword(String macAddress,String password) {
		
		RouterInventory routerInventory = routerInventoryRepository.findByMacAddress(macAddress).orElseThrow(() -> new ResourceNotFoundException("router not found with given Mac Addredd !!"));
		
		/*Customer customer = */customerRepository.findByRouterId(routerInventory.getId()).orElseThrow( () -> new ResourceNotFoundException("customer not found with given macAddress !!"));
		
		String newPassword = null;
        try {
            newPassword = PasswordEncodeDecode.encrypt(password);
        } catch (Exception e) {
            throw new BadApiRequestException(e.getMessage());
        }
		routerInventory.setCpePassword(newPassword);
		routerInventory.setPasswordUpdationTime(LocalDateTime.now());
		
		RouterInventoryDto router = mapper.map(routerInventoryRepository.save(routerInventory), RouterInventoryDto.class);
		
		String changePassword = null;
		try {
			changePassword = PasswordEncodeDecode.decrypt(router.getCpePassword());
		} catch (Exception e) {
			throw new BadApiRequestException(e.getMessage());
		}
	
		return Map.of("Message","Password Change Successfully !!","MacAddress",router.getMacAddress(),"Password",changePassword);
	}



	@Override
	public CustomerDto getCustomerDetailsOfRouter(String macAddress) {
		
		RouterInventory routerInventory = routerInventoryRepository.findByMacAddress(macAddress).orElseThrow(() -> new ResourceNotFoundException("router not found with given Mac Addredd !!"));
		
		Customer customer = customerRepository.findByRouterId(routerInventory.getId()).orElseThrow( () -> new ResourceNotFoundException("customer not found with given macAddress !!"));
		
		return mapper.map(customer, CustomerDto.class);
		
	}
}
