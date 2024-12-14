package com.wpits.services.serviceImpls;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.CustomerDto;
import com.wpits.dtos.EkycCrmMappingDto;
import com.wpits.entities.Customer;
import com.wpits.entities.CustomerDeviceInfo;
import com.wpits.entities.DeviceInventory;
import com.wpits.entities.EkycCrmMapping;
import com.wpits.entities.Partner;
import com.wpits.entities.SimInventory;
import com.wpits.entities.Customer.Status;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.CustomerDeviceInfoRepository;
import com.wpits.repositories.CustomerRepository;
import com.wpits.repositories.DeviceInventoryRepository;
import com.wpits.repositories.EkycCrmMappingRepository;
import com.wpits.repositories.PartnerRepository;
import com.wpits.repositories.SimInventoryRepository;
import com.wpits.services.CustomerService;
import com.wpits.services.EkycCrmMappingService;

@Service
public class EkycCrmMappingServiceImpl implements EkycCrmMappingService {
	
	@Autowired
	private EkycCrmMappingRepository ekycCrmMappingRepository;
	
	@Autowired
	private SimInventoryRepository simInventoryRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private DeviceInventoryRepository deviceInventoryRepository;
	
	@Autowired
	private CustomerDeviceInfoRepository customerDeviceInfoRepository;
	
	private final Random random =new Random();

	@Transactional
	public EkycCrmMappingDto createCustomerFromEkyc(EkycCrmMappingDto ekycCrmMappingDto) {
		
		
		EkycCrmMapping ekyc =null;
		 if (ekycCrmMappingDto == null) {
		        throw new IllegalArgumentException("ekyc CRM mappingDto cannot be null !!");
		    }
		try {
			SimInventory simInventory = simInventoryRepository.findByMsisdn(ekycCrmMappingDto.getMsisdn()).orElseThrow( () -> new ResourceNotFoundException("msisdn not found with given number !!"));
			System.out.println("!!! msisdn : " + simInventory.getMsisdn());
			//System.out.println("@@@@@"+ekycCrmMappingDto);
			System.out.println("@@@ Ekyc :"+ ekycCrmMappingDto);
			EkycCrmMapping ekycCrmMapping = mapper.map(ekycCrmMappingDto, EkycCrmMapping.class);
			//System.out.println("#####"+ekycCrmMapping);
			ekyc = ekycCrmMappingRepository.save(ekycCrmMapping);
			
			//PUSH to CRM
			CustomerDto cust = new CustomerDto();
			cust.setEkycId(ekyc.getEkycId());
			cust.setIdDocId(ekyc.getIdDocId());
			cust.setStreetAddres1(ekyc.getAddress());
			cust.setFirstName(ekyc.getFirstName());
			cust.setMaidenName(ekyc.getMaidenName());
			cust.setLastName(ekyc.getLastName());
			cust.setGender(ekyc.getGender());
			cust.setDateOfBirth(ekyc.getDob());
			cust.setNationality(ekyc.getNationality());
			cust.setCustomerType(ekyc.getRechargeType());
			cust.setUserType(ekyc.getUserType());
			cust.setResidentType(ekyc.getResidentType());
			cust.setEkycToken(ekyc.getToken());
			cust.setOriginalPhotoUrl(ekyc.getOriginalPhotoUrl());
			
			System.out.println("Date" + ekyc.getCreateTime());
			try {
		        LocalDateTime ekycDate = LocalDateTime.parse(ekyc.getCreateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		        cust.setEkycDate(ekycDate);
		    } catch (DateTimeParseException e) {
		        throw new ResourceNotFoundException("pls send correct format of create date from ekyc: " + e.getMessage());
		    }
			//System.out.println(LocalDateTime.parse(ekyc.getCreateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//			LocalDateTime ekycDate = LocalDateTime.parse(ekyc.getCreateTime(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//			System.out.println("ekycDate" + ekycDate);
//			cust.setEkycDate(ekycDate);

			cust.setEkycStatus(ekyc.getStatus());
			cust.setRemark(ekyc.getRemark());
			
			cust.setVip(ekyc.isVip());
			
	//default
			cust.setExcludeAging(0);
			cust.setOptlock(random.nextInt(999999));
			cust.setUseParentPricing(false);
			cust.setCreateDateTime(LocalDateTime.now());
			cust.setDeleted(0);
			cust.setAlternateNumber(ekyc.getAlternateNumber());
			cust.setCity(ekyc.getCity());
			cust.setCountryCode(ekyc.getCountryCode());
			cust.setEmail(ekyc.getEmail());
			cust.setInvoiceChild(ekyc.getInvoiceChild());
			cust.setIsParent(ekyc.getIsParent());
			cust.setMonthlyLimit(ekyc.getMonthlyLimit());
			cust.setNextInvoiceDayOfPeriod(ekyc.getNextInvoiceDayOfPeriod());
			cust.setNextInoviceDate(ekyc.getNextInoviceDate());
			cust.setInvoiceDesign(ekyc.getInvoiceDesign());
			cust.setNotificationInclude(ekyc.getNotificationInclude());
			cust.setParentId(ekyc.getParentId());
			cust.setPersonInitial(ekyc.getPersonInitial());
			cust.setPersonTitle(ekyc.getPersonTitle());
			cust.setRechargeThreshold(ekyc.getRechargeThreshold());
			cust.setReferralFeePaid(ekyc.getReferralFeePaid());
			cust.setElectricityMeterId(ekyc.getElectricityMeterId());
			cust.setServiceType(ekyc.getServiceType());
			if (ekyc.getPartnerId() >0 ){
				//partner
				Partner partner = partnerRepository.findById(ekyc.getPartnerId()).orElseThrow( () -> new ResourceNotFoundException("partner not found  with given id !!"));
				//partner.getBaseUser().getId();
				System.out.println(partner.getId());
				//CustomerService.createCustomer(CustomerDto customerDto, int accountId, int invoiceDeliveryId, int partnerId, int baseUserId, int orderPeriodId, String msisdn, int deviceInventoryId,int routerSerialNo)
				CustomerDto customer = customerService.createCustomer(cust, 1, 1, partner.getId(), partner.getBaseUser().getId(), 1, ekyc.getMsisdn(), 0, 0);
				System.out.println("@@If@@"+customer.getId());
			} else {
				CustomerDto customer = customerService.createCustomer(cust, 1, 1, 0, 0, 1, ekyc.getMsisdn(), 0, 0);
				System.out.println("@@Else@@"+customer.getId());
			}	
		} catch (Exception e) {
			throw new ResourceNotFoundException("Error CRM activate customer from ekyc" + e.getMessage());
		}
		
		
		return mapper.map(ekyc, EkycCrmMappingDto.class);
	}

	@Override
	public ApiResponseMessage createCustomerFromKycForDevice(String token, int deviceId, int partnerId) {
		
		CustomerDeviceInfo deviceInfo = new CustomerDeviceInfo();
		
	    List<Customer> customers = customerRepository.findByEkycToken(token);

	    if (customers.isEmpty()) {
	        throw new ResourceNotFoundException("Customer not found with given token !!");
	    }
		
		Customer customer = customers.get(0);
		deviceInfo.setCustomerId(customer.getId());
		
		DeviceInventory deviceInventory = deviceInventoryRepository.findById(deviceId).orElseThrow(() -> new ResourceNotFoundException("device not found with given id !!"));
		if (deviceInventory.getAllocationDate()!= null) {
			
			throw new ResourceNotFoundException("dear, Partner this device already assigned !!");	
		}
		
		deviceInfo.setDeviceId(deviceId);
		
		if (partnerId > 0) {
			
			Partner partner = partnerRepository.findById(partnerId).orElseThrow( () -> new ResourceNotFoundException("partner not found with given id !!"));
			
			deviceInfo.setPartnerId(partnerId);
			
			if (partnerId != deviceInventory.getPartnerId()) {
				throw new ResourceNotFoundException("dear, Partner this product does not belong to you !!");
			}
		}
		
		deviceInfo.setPurchaseDate(LocalDate.now());
		
		CustomerDeviceInfo response = customerDeviceInfoRepository.save(deviceInfo);
		
		if (response != null) {
			//device segregation ke liye
			deviceInventory.setAllocationDate(LocalDate.now());
			deviceInventoryRepository.save(deviceInventory);
			
			return ApiResponseMessage.builder().message("saved successfully !!").success(true).status(HttpStatus.OK).build();
		
		} else {
			
			return ApiResponseMessage.builder().message("some thing went wrong try again... !!").success(true).status(HttpStatus.BAD_REQUEST).build();

		}
		
	}

	@Override
	public ApiResponseMessage createCustomerFromKycForSim(String token,String msisdn, int partnerId, String customerType, String serviceType) {
		
		Customer customer = new Customer();
		
		if (token == null ) {
			throw new ResourceNotFoundException("exiting customer not found with given token !!");
		}
		
	    List<Customer> customers = customerRepository.findByEkycToken(token);

	    if (customers.isEmpty()) {
	        throw new ResourceNotFoundException("Customer not found with given token !!");
	    }
	    
	    Customer existingcustomer = customers.get(0);
	    
	    
	    SimInventory simInventory = simInventoryRepository.findByMsisdn(msisdn).orElseThrow( () -> new ResourceNotFoundException("Sim not found with given msisdn !!"));
	    
		if (partnerId > 0) {

			Partner partner = partnerRepository.findById(partnerId).orElseThrow(() -> new ResourceNotFoundException("partner not found with given id !!"));

			customer.setParentId(partner.getId());
			customer.setBaseUser(partner.getBaseUser());
			
			if (partnerId != simInventory.getPartnerId()) {
				throw new ResourceNotFoundException("dear, Partner this product does not belong to you !!");
			}
		}

		customer.setReferralFeePaid(0);
		customer.setAutoPaymentType(0);
		customer.setDueDateUnitId(0);
		customer.setDueDateValue(0);
		customer.setDfFm(0);
		customer.setIsParent(existingcustomer.getId());
		customer.setIsParent(existingcustomer.getId());
		customer.setExcludeAging(0);
		customer.setInvoiceChild(0);
		customer.setOptlock(new Random().nextInt(999999));
		customer.setDynamicBalance(0.0);
		customer.setCreditLimit(0.0);
		customer.setAutoRecharge(0.0);
		customer.setUseParentPricing(false);
		customer.setNextInvoiceDayOfPeriod(0);
		customer.setNextInoviceDate(LocalDate.now().plusMonths(1));
		customer.setInvoiceDesign("Neotel Invoice");
		customer.setCreditNotificationLimit1(0.0);
		customer.setCreditNotificationLimit2(0.0);
		customer.setCurrentMonth(LocalDateTime.now());
		customer.setOriginalPhotoUrl(existingcustomer.getOrganizationName() == null ? null : existingcustomer.getOrganizationName());
		customer.setStreetAddres1(existingcustomer.getStreetAddres1() == null ? null : existingcustomer.getStreetAddres1());
		customer.setStreetAddres2(existingcustomer.getStreetAddres2() == null ? null : existingcustomer.getStreetAddres2());
		customer.setCity(existingcustomer.getCity() == null ? null : existingcustomer.getCity());
		customer.setStateProvince(existingcustomer.getStateProvince() == null ? null : existingcustomer.getStateProvince());
		customer.setPostalCode(existingcustomer.getPostalCode() == null ? null : existingcustomer.getPostalCode());
		customer.setCountryCode(existingcustomer.getCountryCode() == null ? null :existingcustomer.getCountryCode());
		customer.setLastName(existingcustomer.getLastName() == null ? null : existingcustomer.getLastName());
		customer.setFirstName(existingcustomer.getFirstName() == null ? null : existingcustomer.getFirstName());
		customer.setPersonInitial(existingcustomer.getPersonInitial() == null ? null : existingcustomer.getPersonInitial());
		customer.setPersonTitle(existingcustomer.getPersonTitle() == null ? null : existingcustomer.getPersonTitle());
		customer.setPhoneCountryCode(existingcustomer.getPhoneCountryCode() == 0 ? 0 : existingcustomer.getPhoneCountryCode());
		customer.setPhoneAreaCode(existingcustomer.getPhoneAreaCode() == 0 ? 0 : existingcustomer.getPhoneAreaCode());
		customer.setPhonePhoneNumber(existingcustomer.getPhonePhoneNumber() == null ? null : existingcustomer.getPhonePhoneNumber());
		customer.setFaxAreaCode(existingcustomer.getFaxAreaCode() == 0 ? 0 : existingcustomer.getFaxAreaCode());
		customer.setFaxPhoneNumber(existingcustomer.getFaxPhoneNumber() == null ? null : existingcustomer.getFaxPhoneNumber());
		customer.setEmail(existingcustomer.getEmail() == null ? null : existingcustomer.getEmail());
		customer.setCreateDateTime(LocalDateTime.now());
		customer.setDeleted(0);
		customer.setNotificationInclude(0);
		customer.setSimInventory(simInventory);
		customer.setCustomerType(customerType);
		customer.setGender(existingcustomer.getGender() == null ? null : existingcustomer.getGender());
		customer.setEkycStatus(existingcustomer.getEkycStatus() == null ? null : existingcustomer.getEkycStatus());
		customer.setEkycToken(token);
		customer.setEkycDate(existingcustomer.getEkycDate() == null ? null : existingcustomer.getEkycDate());
		customer.setEkycId(existingcustomer.getEkycId() == null ? null : existingcustomer.getEkycId());
		customer.setIdDocId(existingcustomer.getIdDocId() == null ? null : existingcustomer.getIdDocId());
		customer.setUserType(existingcustomer.getUserType() == null ? null : existingcustomer.getUserType());
		customer.setResidentType(existingcustomer.getResidentType() == null ? null : existingcustomer.getResidentType());
		customer.setOriginalPhotoUrl(existingcustomer.getOriginalPhotoUrl() == null ? null : existingcustomer.getOriginalPhotoUrl());
		customer.setMaidenName(existingcustomer.getMaidenName() == null ? null : existingcustomer.getMaidenName());
		customer.setNationality(existingcustomer.getNationality() == null ? null : existingcustomer.getNationality());
		customer.setRemark(existingcustomer.getRemark() == null ? null : existingcustomer.getRemark());
		customer.setAlternateNumber(existingcustomer.getAlternateNumber() == null ? null : existingcustomer.getAlternateNumber());
		customer.setLandlineNumber(existingcustomer.getLandlineNumber() == null ? null : existingcustomer.getLandlineNumber());
		customer.setDateOfBirth(existingcustomer.getDateOfBirth() == null ? null : existingcustomer.getDateOfBirth());
		customer.setVatId(existingcustomer.getVatId() == null ? null : existingcustomer.getVatId());
		customer.setProfession(existingcustomer.getProfession() == null ? null : existingcustomer.getProfession());
		customer.setMaritalStatus(existingcustomer.getMaritalStatus() == null ? null : existingcustomer.getMaritalStatus());
		customer.setImageName(existingcustomer.getImageName() == null ? null : existingcustomer.getImageName());
		customer.setAccountType(existingcustomer.getAccountType() == null ? null : existingcustomer.getAccountType());
		customer.setInvoiceDeliveryMethod(existingcustomer.getInvoiceDeliveryMethod() == null ? null : existingcustomer.getInvoiceDeliveryMethod());
		customer.setOrderPeriod(existingcustomer.getOrderPeriod() == null ? null : existingcustomer.getOrderPeriod());
		//customer.setDeviceInventory(existingcustomer.getDeviceInventory() == null ? null : existingcustomer.getDeviceInventory());
		customer.setElectricityMeterId(existingcustomer.getElectricityMeterId() == null ? null : existingcustomer.getElectricityMeterId());
		customer.setServiceStatus(Status.ACTIVE);
		customer.setServiceType(serviceType);	
		
//		customer.setCreateDateTime(LocalDateTime.now());
//		customer.setEkycDate(LocalDateTime.now());

		customerRepository.save(customer);
		// Allocation Date
		simInventoryRepository.updateAllocationDate(LocalDateTime.now(), msisdn);

		return null;

	}

}
