package com.wpits.services.serviceImpls;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
/*import static java.time.temporal.ChronoUnit.DAYS;*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.CustomerDetailWithPack;
import com.wpits.dtos.CustomerDto;
import com.wpits.dtos.CustomerPackDetail;
import com.wpits.dtos.ErpCustomerRequestDto;
import com.wpits.dtos.ImageResponse;
import com.wpits.dtos.PartnerSaleReportResponse;
import com.wpits.dtos.ResponseMessage;
import com.wpits.entities.AccountType;
import com.wpits.entities.BaseUser;
import com.wpits.entities.Customer;
import com.wpits.entities.DeviceInventory;
import com.wpits.entities.Invoice;
import com.wpits.entities.InvoiceDeliveryMethod;
import com.wpits.entities.OrderPeriod;
import com.wpits.entities.Partner;
import com.wpits.entities.RouterInventory;
import com.wpits.entities.SimInventory;
import com.wpits.exceptions.BadApiRequestException;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.AccountTypeRepository;
import com.wpits.repositories.BaseUserRepository;
import com.wpits.repositories.CustomerRepository;
import com.wpits.repositories.DeviceInventoryRepository;
import com.wpits.repositories.InvoiceDeliveryMethodRepository;
import com.wpits.repositories.InvoiceRepository;
import com.wpits.repositories.SimInventoryRepository;
import com.wpits.repositories.OrderPeriodRepository;
import com.wpits.repositories.PartnerRepository;
import com.wpits.repositories.RouterInventoryRepository;
import com.wpits.services.CustomerService;
import com.wpits.services.FileService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private AccountTypeRepository accountTypeRepository;
	@Autowired
	private InvoiceDeliveryMethodRepository invoiceDeliveryMethodRepository;
	@Autowired
	private PartnerRepository partnerRepository;
	@Autowired
	private BaseUserRepository baseUserRepository;
	@Autowired
	private OrderPeriodRepository orderPeriodRepository;
	@Autowired
	private SimInventoryRepository simInventoryRepository;
	@Autowired
	private DeviceInventoryRepository deviceInventoryRepository;
	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private RouterInventoryRepository routerInventoryRepository;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private FileService fileService;

	@Value("${abmf.server.url}")
	private String postpaidUserStatusUrl;

	@Value("${abmf.prepaid.pack.info.server.url}")
	private String prepaidPackInfoUrl;

	@Value("${abmf.postpaid.pack.info.server.url}")
	private String postpaidPackInfoUrl;

	@Value("${customer.profile.image.path}")
	private String imagePath;

	private final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	private final Random random = new Random();
	
	private static final String POSTPAID ="postpaid";

	@Override
	public CustomerDto createCustomer(CustomerDto customerDto, int accountId, int invoiceDeliveryId, int partnerId,
			int baseUserId, int orderPeriodId, String msisdn, int deviceInventoryId,int routerSerialNo) {

		try {
			
			AccountType accountType = accountTypeRepository.findById(accountId)
					.orElseThrow(() -> new ResourceNotFoundException("account type not found with given id !!"));

			InvoiceDeliveryMethod invoiceDeliveryMethod = invoiceDeliveryMethodRepository.findById(invoiceDeliveryId)
					.orElseThrow(() -> new ResourceNotFoundException("invoice delevery method not found with given id !!"));
			//		Partner partner = partnerRepository.findById(partnerId).orElseThrow( () -> new ResourceNotFoundException("partner not found with given id !!"));
			//		BaseUser baseUser = baseUserRepository.findById(baseUserId).orElseThrow( () -> new ResourceNotFoundException("base user not found with given id !!"));
			OrderPeriod orderPeriod = orderPeriodRepository.findById(orderPeriodId)
					.orElseThrow(() -> new ResourceNotFoundException("order period not found with given id !!"));
			//		MsisdnInventory msisdnInventory = msisdnInventoryRepository.findByMsisdn(msisdn).orElseThrow( () -> new ResourceNotFoundException("msisdn inventory record not found with given msisdn !!"));
			//		DeviceInventory deviceInventory = deviceInventoryRepository.findById(deviceInventoryId).orElseThrow( () -> new ResourceNotFoundException("device inventory record not found with given id !!"));

			Customer customer = mapper.map(customerDto, Customer.class);

			/*if (customer.getCustomerType() != null && customer.getCustomerType().equalsIgnoreCase(POSTPAID)) {
				customer.setServiceStatus(Customer.Status.ACTIVE);
			}*/
			customer.setServiceStatus(Customer.Status.ACTIVE);
			customer.setAccountType(accountType);
			customer.setInvoiceDeliveryMethod(invoiceDeliveryMethod);
			if (partnerId >= 1) {
				Partner partner = partnerRepository.findById(partnerId)
						.orElseThrow(() -> new ResourceNotFoundException("partner not found with given id !!"));
				customer.setPartner(partner);
			}

			if (baseUserId >= 1) {
				BaseUser baseUser = baseUserRepository.findById(baseUserId)
						.orElseThrow(() -> new ResourceNotFoundException("base user not found with given id !!"));
				customer.setBaseUser(baseUser);
			}

			customer.setOrderPeriod(orderPeriod);

			if (msisdn != null && deviceInventoryId >= 1) {
				SimInventory simInventory = simInventoryRepository.findByMsisdn(msisdn).orElseThrow(
						() -> new ResourceNotFoundException("sim inventroy record not foun with given msisdn !!"));

				DeviceInventory deviceInventory = deviceInventoryRepository.findById(deviceInventoryId).orElseThrow(
						() -> new ResourceNotFoundException("device inventory record not found with given id !!"));

				System.out.println("sim and device purchase");
				if (partnerId != simInventory.getPartnerId()) {
					throw new ResourceNotFoundException("dear, Partner this sim product does not belong to you  !!");
				}

				if (partnerId != deviceInventory.getPartnerId()) {
					throw new ResourceNotFoundException("dear, Partner this device product does not belong to you !! ");
				}

				//pospaid default 1 RS set and update when assign pack	
				if (customer.getCustomerType().equalsIgnoreCase(POSTPAID)) {

					customer.setMonthlyLimit(customer.getMonthlyLimit() == null ? 1.0 : customer.getMonthlyLimit());
				}

				customer.setSimInventory(simInventory);
				customer.setDeviceInventory(deviceInventory);

				customer.setOptlock(random.nextInt(999999));
				//customer.setNextInoviceDate(LocalDate.now().plusMonths(1));
				customer.setCurrentMonth(LocalDateTime.now());
				customer.setCreateDateTime(LocalDateTime.now());
				//customer.setEkycDate(LocalDateTime.now());
				customer.setEkycDate(customer.getEkycDate() == null ? LocalDateTime.now() : customer.getEkycDate());

				//parent child to PostPaid default
				customer.setIsParent(0);
				customer.setUseParentPricing(false);
				customer.setParentId(0);
				
				Customer savedCustomer = customerRepository.save(customer);
				//Allocation Date
				simInventoryRepository.updateAllocationDate(LocalDateTime.now(), msisdn);

				//
				deviceInventory.setAllocationDate(LocalDate.now());
				deviceInventoryRepository.save(deviceInventory);

				return mapper.map(savedCustomer, CustomerDto.class);

			} else if (msisdn != null) {
				SimInventory simInventory = simInventoryRepository.findByMsisdn(msisdn).orElseThrow(
						() -> new ResourceNotFoundException("sim inventroy record not foun with given msisdn !!"));

				System.out.println("only sim purchase");

				if (partnerId != simInventory.getPartnerId()) {
					throw new ResourceNotFoundException("dear, Partner this sim product does not belong to you !!");
				}

				//pospaid default 1 RS/$ set and update when assign pack
				if (customer.getCustomerType().equalsIgnoreCase(POSTPAID)) {

					customer.setMonthlyLimit(customer.getMonthlyLimit() == null ? 1.0 : customer.getMonthlyLimit());
				}

				customer.setSimInventory(simInventory);

				customer.setOptlock(random.nextInt(999999));
				//customer.setNextInoviceDate(LocalDate.now().plusMonths(1));
				customer.setCurrentMonth(LocalDateTime.now());
				customer.setCreateDateTime(LocalDateTime.now());
				customer.setEkycDate(LocalDateTime.now());
				
				customer.setIsParent(0);
				customer.setUseParentPricing(false);
				customer.setParentId(0);

				Customer savedCustomer = customerRepository.save(customer);
				//Allocation Date
				simInventoryRepository.updateAllocationDate(LocalDateTime.now(), msisdn);

				return mapper.map(savedCustomer, CustomerDto.class);

			} else if (deviceInventoryId >= 1) {

				DeviceInventory deviceInventory = deviceInventoryRepository.findById(deviceInventoryId).orElseThrow(
						() -> new ResourceNotFoundException("device inventory record not found with given id !!"));

				System.out.println("only device purchase");

				if (partnerId != deviceInventory.getPartnerId()) {
					throw new ResourceNotFoundException("dear, Partner this device product does not belong to you !!");
				}

				customer.setDeviceInventory(deviceInventory);

				customer.setOptlock(random.nextInt(999999));
				//customer.setNextInoviceDate(LocalDate.now().plusMonths(1));
				customer.setCurrentMonth(LocalDateTime.now());
				customer.setCreateDateTime(LocalDateTime.now());
				customer.setEkycDate(LocalDateTime.now());
				
				customer.setIsParent(0);
				customer.setUseParentPricing(false);
				customer.setParentId(0);

				Customer savedCustomer = customerRepository.save(customer);

				//
				deviceInventory.setAllocationDate(LocalDate.now());
				deviceInventoryRepository.save(deviceInventory);

				return mapper.map(savedCustomer, CustomerDto.class);

			}else if (routerSerialNo > 0) {
				
				System.out.println("router purchase");
				
				//RouterInventory routerInventory = null;
				
				if(routerSerialNo > 0) {
					//System.out.println(routerSerialNo);
					
					RouterInventory routerInventory = routerInventoryRepository.findBySerialNumber(routerSerialNo).orElseThrow( () -> new ResourceNotFoundException("router not found with given serial number !!"));
					customer.setRouterInventory(routerInventory);
				}
				
				customer.setOptlock(random.nextInt(999999));
				customer.setCurrentMonth(LocalDateTime.now());
				customer.setCreateDateTime(LocalDateTime.now());
				
				customer.setIsParent(0);
				customer.setUseParentPricing(false);
				customer.setParentId(0);
				
				Customer savedCustomer = customerRepository.save(customer);
				
				//routerInventory.setAllocationDate(LocalDateTime.now());
				//routerInventoryRepository.save(routerInventory);

				return mapper.map(savedCustomer, CustomerDto.class);
				
			}
			else {
				
				//throw new BadApiRequestException("pls choose any product like Sim & device, etc !!");
				customer.setOptlock(random.nextInt(999999));
				customer.setCurrentMonth(LocalDateTime.now());
				customer.setCreateDateTime(LocalDateTime.now());
				
				customer.setIsParent(0);
				customer.setUseParentPricing(false);
				customer.setParentId(0);
				
				Customer savedCustomer = customerRepository.save(customer);
				
				return mapper.map(savedCustomer, CustomerDto.class);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			//logger.error("Error : {} ", e.getMessage());
			throw new BadApiRequestException(e.getMessage());
			
		}
		

	}

	@Override
	public CustomerDto updateCustomer(CustomerDto customerDto, int customerId) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException(" customer not found with given id !!"));

		customer.setReferralFeePaid(customerDto.getReferralFeePaid());
		customer.setAutoPaymentType(customerDto.getAutoPaymentType());
		customer.setDueDateUnitId(customerDto.getDueDateUnitId());
		customer.setDueDateValue(customerDto.getDueDateValue());
		customer.setDfFm(customerDto.getDfFm());
		customer.setParentId(customerDto.getParentId());
		customer.setIsParent(customerDto.getIsParent());
		customer.setExcludeAging(customerDto.getExcludeAging());
		customer.setInvoiceChild(customerDto.getInvoiceChild());
		customer.setOptlock(customerDto.getOptlock());
		customer.setDynamicBalance(customerDto.getDynamicBalance());
		customer.setCreditLimit(customerDto.getCreditLimit());
		customer.setAutoRecharge(customerDto.getAutoRecharge());
		customer.setUseParentPricing(customerDto.getUseParentPricing());
		customer.setNextInvoiceDayOfPeriod(customerDto.getNextInvoiceDayOfPeriod());
		customer.setNextInoviceDate(customerDto.getNextInoviceDate());
		customer.setInvoiceDesign(customerDto.getInvoiceDesign());
		customer.setCreditNotificationLimit1(customerDto.getCreditNotificationLimit1());
		customer.setCreditNotificationLimit2(customerDto.getCreditNotificationLimit2());
		customer.setRechargeThreshold(customerDto.getRechargeThreshold());
		customer.setMonthlyLimit(customerDto.getMonthlyLimit());
		customer.setCurrentMonthlyAmount(customerDto.getCurrentMonthlyAmount());
		customer.setCurrentMonth(LocalDateTime.now());
		customer.setOrganizationName(customerDto.getOrganizationName());
		customer.setStreetAddres1(customerDto.getStreetAddres1());
		customer.setStreetAddres2(customerDto.getStreetAddres2());
		customer.setCity(customerDto.getCity());
		customer.setStateProvince(customerDto.getStateProvince());
		customer.setPostalCode(customerDto.getPostalCode());
		customer.setCountryCode(customerDto.getCountryCode());
		customer.setLastName(customerDto.getLastName());
		customer.setFirstName(customerDto.getFirstName());
		customer.setPersonInitial(customerDto.getPersonInitial());
		customer.setPersonTitle(customerDto.getPersonTitle());
		customer.setPhoneCountryCode(customerDto.getPhoneCountryCode());
		customer.setPhoneAreaCode(customerDto.getPhoneAreaCode());
		customer.setPhonePhoneNumber(customerDto.getPhonePhoneNumber());
		customer.setFaxCountryCode(customerDto.getFaxCountryCode());
		customer.setFaxAreaCode(customerDto.getFaxAreaCode());
		customer.setFaxPhoneNumber(customerDto.getFaxPhoneNumber());
		customer.setEmail(customerDto.getEmail());
		customer.setCreateDateTime(LocalDateTime.now());
		customer.setDeleted(customerDto.getDeleted());
		customer.setNotificationInclude(customerDto.getNotificationInclude());
		customer.setCustomerType(customerDto.getCustomerType());
		customer.setGender(customerDto.getGender());
		customer.setEkycStatus(customerDto.getEkycStatus());
		customer.setEkycToken(customerDto.getEkycToken());
		customer.setEkycDate(LocalDateTime.now());
		customer.setAlternateNumber(customerDto.getAlternateNumber());
		customer.setLandlineNumber(customerDto.getLandlineNumber());
		customer.setDateOfBirth(customerDto.getDateOfBirth());
		customer.setVatId(customerDto.getVatId());
		customer.setProfession(customerDto.getProfession());
		customer.setMaritalStatus(customerDto.getMaritalStatus());
		customer.setImageName(customerDto.getImageName());
		customer.setServiceStatus(customer.getServiceStatus());
		Customer savedCustomer = customerRepository.save(customer);

		return mapper.map(savedCustomer, CustomerDto.class);

	}

	@Override
	public List<CustomerDto> getAllCustomers(Integer pageNo, Integer pageSize, String sortBy) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		
		Page<Customer> pagedResult = customerRepository.findAll(pageable);
		
		if(pagedResult.hasContent()) {
            return pagedResult.getContent().stream().map(customer -> mapper.map(customer, CustomerDto.class)).collect(Collectors.toList());
        } else {
            return new ArrayList<CustomerDto>();
        }

	}

	@Override
	public CustomerDto getSingleCustomer(int customerId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("customer not found with given id !!!"));
		return mapper.map(customer, CustomerDto.class);
	}

	@Override
	public void deleteCustomer(int customerId) {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("customer not found with given id !"));

		//delete customer image
		//String fullPath = imagePath + File.separator + customer.getImageName();
		String fullPath = imagePath + customer.getImageName();
		System.out.println(fullPath);

		try {

			Path path = Paths.get(fullPath);
			Files.delete(path);
		} catch (NoSuchFileException e) {
			logger.info("Customer Image not found in folder");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		customerRepository.delete(customer);
	}

	// customer details with pack
	@Override
	public List<CustomerDetailWithPack> getAllCustomerDetailWithPack() {

		List<CustomerDetailWithPack> customerDetailWithPacks = new ArrayList<>();

		try {
			List<Customer> customers = customerRepository.findAll();

			for (Customer customer : customers) {

				CustomerDetailWithPack custPackDetail = new CustomerDetailWithPack();

				custPackDetail.setId(customer.getId());
				custPackDetail.setReferralFeePaid(customer.getReferralFeePaid());
				custPackDetail.setAutoPaymentType(customer.getAutoPaymentType());
				custPackDetail.setDueDateUnitId(customer.getDueDateUnitId());
				custPackDetail.setDueDateValue(customer.getDueDateValue());
				custPackDetail.setDfFm(customer.getDfFm());
				custPackDetail.setParentId(customer.getParentId());
				custPackDetail.setIsParent(customer.getIsParent());
				custPackDetail.setExcludeAging(customer.getExcludeAging());
				custPackDetail.setInvoiceChild(customer.getInvoiceChild());
				custPackDetail.setOptlock(customer.getOptlock());
				custPackDetail.setDynamicBalance(customer.getDynamicBalance());
				custPackDetail.setCreditLimit(customer.getCreditLimit());
				custPackDetail.setAutoRecharge(customer.getAutoRecharge());
				custPackDetail.setUseParentPricing(customer.getUseParentPricing());
				custPackDetail.setNextInvoiceDayOfPeriod(customer.getNextInvoiceDayOfPeriod());
				custPackDetail.setNextInoviceDate(customer.getNextInoviceDate());
				custPackDetail.setInvoiceDesign(customer.getInvoiceDesign());
				custPackDetail.setCreditNotificationLimit1(customer.getCreditNotificationLimit1());
				custPackDetail.setCreditNotificationLimit2(customer.getCreditNotificationLimit2());
				custPackDetail.setRechargeThreshold(customer.getRechargeThreshold());
				custPackDetail.setMonthlyLimit(customer.getMonthlyLimit());
				custPackDetail.setCurrentMonthlyAmount(customer.getCurrentMonthlyAmount());
				custPackDetail.setCurrentMonth(customer.getCurrentMonth());
				custPackDetail.setOrganizationName(customer.getOrganizationName());
				custPackDetail.setStreetAddres1(customer.getStreetAddres1());
				custPackDetail.setStreetAddres2(customer.getStreetAddres2());
				custPackDetail.setCity(customer.getCity());
				custPackDetail.setStateProvince(customer.getStateProvince());
				custPackDetail.setStateProvince(customer.getStateProvince());
				custPackDetail.setPostalCode(customer.getPostalCode());
				custPackDetail.setCountryCode(customer.getCountryCode());
				custPackDetail.setLastName(customer.getLastName());
				custPackDetail.setFirstName(customer.getFirstName());
				custPackDetail.setPersonInitial(customer.getPersonInitial());
				custPackDetail.setPersonTitle(customer.getPersonTitle());
				custPackDetail.setPhoneCountryCode(customer.getPhoneCountryCode());
				custPackDetail.setPhoneAreaCode(customer.getPhoneAreaCode());
				custPackDetail.setPhonePhoneNumber(customer.getPhonePhoneNumber());
				custPackDetail.setFaxCountryCode(customer.getFaxCountryCode());
				custPackDetail.setFaxAreaCode(customer.getFaxAreaCode());
				custPackDetail.setFaxPhoneNumber(customer.getFaxPhoneNumber());
				custPackDetail.setEmail(customer.getEmail());
				custPackDetail.setCreateDateTime(customer.getCreateDateTime());
				custPackDetail.setDeleted(customer.getDeleted());
				custPackDetail.setNotificationInclude(customer.getNotificationInclude());
				custPackDetail.setServiceStatus(customer.getServiceStatus());
				custPackDetail.setSimInventory(customer.getSimInventory() == null ? null : customer.getSimInventory());
				custPackDetail.setCustomerType(customer.getCustomerType());
				custPackDetail.setGender(customer.getGender());
				custPackDetail.setEkycStatus(customer.getEkycStatus());
				custPackDetail.setEkycToken(customer.getEkycToken());
				custPackDetail.setEkycDate(customer.getEkycDate());
				custPackDetail.setAlternateNumber(customer.getAlternateNumber());
				custPackDetail.setLandlineNumber(customer.getLandlineNumber());
				custPackDetail.setDateOfBirth(customer.getDateOfBirth());
				custPackDetail.setVatId(customer.getVatId());
				custPackDetail.setProfession(customer.getProfession());
				custPackDetail.setMaritalStatus(customer.getMaritalStatus());
				custPackDetail.setImageName(customer.getImageName());
				custPackDetail
						.setAccountType(customer.getAccountType() == null ? 0 : customer.getAccountType().getId());
				custPackDetail.setInvoiceDeliveryMethod(
						customer.getInvoiceDeliveryMethod() == null ? 0 : customer.getInvoiceDeliveryMethod().getId());
				custPackDetail.setPartner(customer.getPartner() == null ? 0 : customer.getPartner().getId());
				custPackDetail.setBaseUser(customer.getBaseUser() == null ? 0 : customer.getBaseUser().getId());
				custPackDetail
						.setOrderPeriod(customer.getOrderPeriod() == null ? 0 : customer.getOrderPeriod().getId());
				custPackDetail.setDeviceInventory(
						customer.getDeviceInventory() == null ? 0 : customer.getDeviceInventory().getId());

				if (customer.getCustomerType() != null && customer.getCustomerType().equalsIgnoreCase("prepaid")
						|| customer.getCustomerType().equalsIgnoreCase(POSTPAID)) {

					if (customer.getCustomerType().equalsIgnoreCase("prepaid")) {
						// mufij ki api call
						String PrepaidUrl = prepaidPackInfoUrl + customer.getId();

						try {

							RestTemplate restTemplate = new RestTemplate();
							ResponseEntity<CustomerPackDetail> prepaidApiResponse = restTemplate
									.getForEntity(PrepaidUrl, CustomerPackDetail.class);
							System.out.println("prepaidApiResponse" + prepaidApiResponse.getStatusCode());

							if (prepaidApiResponse.getStatusCode() == HttpStatus.OK) {

								CustomerPackDetail packDetail = prepaidApiResponse.getBody();
								if(packDetail != null) {
								custPackDetail.setPack_name(packDetail.getPack_name());
								custPackDetail.setActivation_date(packDetail.getActivation_date());
								custPackDetail.setExpiration_date(packDetail.getExpiration_date());
								}
							} else {

								custPackDetail.setPack_name("null");
								custPackDetail.setActivation_date("null");
								custPackDetail.setExpiration_date("null");
							}

						} catch (Exception e) {
							System.err.println("##########" + e.getMessage());
							custPackDetail.setPack_name("null");
							custPackDetail.setActivation_date("null");
							custPackDetail.setExpiration_date("null");
						}

					} else {

						try {

							String PostpaidUrl = postpaidPackInfoUrl + customer.getId();

							RestTemplate restTemplate = new RestTemplate();
							ResponseEntity<CustomerPackDetail> prepaidApiResponse = restTemplate
									.getForEntity(PostpaidUrl, CustomerPackDetail.class);
							System.out.println("prepaidApiResponse" + prepaidApiResponse.getStatusCode());

							if (prepaidApiResponse.getStatusCode() == HttpStatus.OK) {

								CustomerPackDetail packDetail = prepaidApiResponse.getBody();
								if(packDetail != null) {
							
								custPackDetail.setPack_name(packDetail.getPack_name());
								custPackDetail.setActivation_date(packDetail.getActivation_date());
								custPackDetail.setExpiration_date(packDetail.getExpiration_date());

								}
							} else {

								custPackDetail.setPack_name("null");
								custPackDetail.setActivation_date("null");
								custPackDetail.setExpiration_date("null");
							}

						} catch (Exception e) {
							System.err.println("@@@@@@@" + e.getMessage());
							custPackDetail.setPack_name("null");
							custPackDetail.setActivation_date("null");
							custPackDetail.setExpiration_date("null");
						}

					}
				} else {
					custPackDetail.setPack_name("null");
					custPackDetail.setActivation_date("null");
					custPackDetail.setExpiration_date("null");
				}

				customerDetailWithPacks.add(custPackDetail);
			}

		} catch (Exception e) {
			System.err.println("!!!!!!!!!" + e.getMessage());
			// e.printStackTrace();
		}

		return customerDetailWithPacks;
	}

	@Override
	public CustomerDto getCustomerByMsisdn(String msisdn) {
		//Customer customer = customerRepository.FindByMsisdn(msisdn).orElseThrow( () -> new ResourceNotFoundException("customer not found with given msisdn !!"));
		Customer customer = customerRepository.findByMsisdn(msisdn).orElse(null);
		System.out.println(customer);
		if (customer == null) {
			throw new ResourceNotFoundException("customer not found with given msisdn !!");
		}
		return mapper.map(customer, CustomerDto.class);
	}

	@Override
	public PartnerSaleReportResponse getPartnerProductSaleReport(int partnerId) {

		//List<Integer> prepaidList = new ArrayList<>();
		PartnerSaleReportResponse response = new PartnerSaleReportResponse();

		int prepaidCount = 0;
		int postpaidCount = 0;
		int deviceCount = 0;

		Partner partner = partnerRepository.findById(partnerId)
				.orElseThrow(() -> new ResourceNotFoundException("partner not found with given id !!"));

		List<Customer> customer = customerRepository.findByPartner(partner)
				.orElseThrow(() -> new ResourceNotFoundException("partner not sell any product to customer !!"));

		for (Customer cust : customer) {

			if (cust.getCustomerType().equalsIgnoreCase("prepaid")) {
				//prepaidList.add()

				prepaidCount++;

			} else if (cust.getCustomerType().equalsIgnoreCase(POSTPAID)) {

				postpaidCount++;
			}
			if (cust.getDeviceInventory() != null) {
				deviceCount++;
			}
		}
		//System.out.println(prepaidCount);
		response.setPrepaidCustomer(prepaidCount);
		response.setPostpaidCustomer(postpaidCount);
		response.setDeviceCustomer(deviceCount);
		System.out.println("total product sell : " + prepaidCount + postpaidCount + deviceCount);
		response.setTotalCustomerOnboard(prepaidCount + postpaidCount + deviceCount);

		return response;
	}

	@Override
	public void checkPostpaidCustomerPayment() {
		List<Customer> customers = customerRepository.findAll();

		List<Customer> postpaidCustomers = customers.stream()
				.filter(customer -> customer.getCustomerType().equalsIgnoreCase(POSTPAID))
				.collect(Collectors.toList());
		System.out.println("postpaidCustomers : " + postpaidCustomers.size());

		List<Customer> invoceCustomers = postpaidCustomers.stream()
				.filter(customer -> customer.getNextInoviceDate() == LocalDate.now()).collect(Collectors.toList());
		System.out.println("invoceCustomers : " + invoceCustomers.size());

	}

	@Override
	public void updateCustomerStatus() {

		LocalDate today = LocalDate.now();
		System.out.println("Customer Service Status Cron task date time : " + today);

		List<Invoice> unpaidInvoices = invoiceRepository.findByDueDateBeforeAndStatus(today, Invoice.Status.UNPAID);
		System.out.println("unPaid invoice count : " + unpaidInvoices.size());
		
		for (Invoice invoice : unpaidInvoices) {
			System.out.println("invoice customerID :" + invoice.getCustomerId());

			Customer customer = customerRepository.findById(invoice.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException(" customer not found with given id !!"));
			System.out.println("customer Id : " + customer.getId());

			//check date today not above dueDate
			/*if (today.isAfter(invoice.getDueDate())) {
			
				if (customer.isVip() && customer.getServiceStatus() == Customer.Status.ACTIVE ) {
			
					customer.setServiceStatus(Customer.Status.ACTIVE);
					
				}else if(!customer.isVip() && customer.getServiceStatus() == Customer.Status.ACTIVE ) {
					
					customer.setServiceStatus(Customer.Status.BLOCKED);
				}
			}*/
			if (today.isAfter(invoice.getDueDate()) && !customer.isVip() && customer.getServiceStatus() == Customer.Status.ACTIVE) {
					
					customer.setServiceStatus(Customer.Status.BLOCKED);
				}
			
			// VIP /Normal customer  if Blocked any reason 
			long daysBetween = ChronoUnit.DAYS.between(invoice.getDueDate(), today);
			System.out.println("daysBetween today and dueDate : " + daysBetween);
			
			if (daysBetween > 10 && customer.getServiceStatus() == Customer.Status.BLOCKED) {

				customer.setServiceStatus(Customer.Status.SUSPEND);
			} 
			
			customerRepository.save(customer);
		}
	}

	@Override
	public List<CustomerDto> searchByKeyword(String keyword) {

		List<Customer> customers = customerRepository.findByFirstNameContaining(keyword);
		customers.forEach(cust -> System.out.println(cust.getFirstName()));

		return customers.stream().map(customer -> mapper.map(customer, CustomerDto.class)).collect(Collectors.toList());
	}

	@Override
	public List<CustomerDto> searchCustomers(String keyword) {

		List<Customer> customers = customerRepository.searchCustomers(keyword);
		System.out.println("customers  : " + customers.size());
		return customers.stream().map(customer -> mapper.map(customer, CustomerDto.class)).collect(Collectors.toList());
	}

	@Override
	public void shareCustomerStatus() {
		//		int count=0;
		//String URL = "http://172.5.10.2:9699/api/postpaid/account/update/user/status";
		System.out.println(postpaidUserStatusUrl);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> request = new HashMap<>();

		List<Customer> customers = customerRepository.findByCustomerTypeAndServiceStatusNot(POSTPAID,Customer.Status.ACTIVE);
		System.out.println("Customers : " + customers.size());

		/*List<Customer> postpaidCustomer = customers.stream()
				.filter(customer -> customer.getCustomerType().equalsIgnoreCase(POSTPAID))
				.collect(Collectors.toList());
		System.out.println("PostPaid : " + postpaidCustomer.size());*/
		
		for (Customer customer : customers) {
		
			request.put("customer_id", customer.getId());
			request.put("user_status", customer.getServiceStatus());
		
			try {
				request.forEach((k, v) -> System.out.println((k + ":" + v)));
		
				HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(request, headers);
		
				ResponseEntity<String> response = restTemplate.exchange(postpaidUserStatusUrl, HttpMethod.PUT,
						requestEntity, String.class);
				
				if (response.getStatusCode().is2xxSuccessful()) {
					System.out.println("Response : " + response.getBody());
				}
				
			} catch (Exception e) {
				System.err.println("URL : " + postpaidUserStatusUrl + "\n" + "Exception : " + e.getMessage());
				//continue;
			}
		}

		/*String imsi = null;
		String msisdn = null;
		
		String URL = "http://172.17.1.11:9698/api/set/subscriber/status/and/category?imsi="+imsi+"&msisdn="+msisdn;
		
		System.out.println(URL);
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		Map<String, Object> request = new HashMap<>();
		
		List<Customer> customers = customerRepository.findAll();
		System.out.println("Customers : " + customers.size());
		
		for (Customer customr : customers) {
		
			imsi = customr.getSimInventory().getImsi();
			msisdn = customr.getSimInventory().getMsisdn();
			
						SimInventory simInventory = customr.getSimInventory();
						msisdn=simInventory.getMsisdn();
						imsi =simInventory.getImsi();
		//			count ++;
		//			System.out.println("hiiii : " +count);
			request.put("status", customr.getServiceStatus());
			request.put("category", customr.getSimInventory().getCategory());
		
			System.out.println("***************"+URL+"****************");
			try {
				request.forEach((k, v) -> System.out.println((k + ":" + v)));
		
				HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(request, headers);
		
				ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.PUT, requestEntity,String.class);
				System.out.println("Response : " + response.getBody().toString());
			} catch (Exception e) {
				System.err.println("URL : " + URL + "\n" + "Exception : " + e.getMessage());
				continue;
			}
		}*/
	}

	@Override
	public void updateMonthlyAmountByPackAssign(String msisdn, double amount) {
		
		LocalDate currentDate = LocalDate.now();
		
		System.out.println("----------Post Paid Plans-------");
		//chk sim status
		SimInventory simInventory = simInventoryRepository.findByMsisdn(msisdn).orElseThrow(() -> new ResourceNotFoundException("sim not found with given msisdn !!"));

		if (simInventory.getActivationStatus() == null || !simInventory.getActivationStatus().equalsIgnoreCase("ACTIVATE")) {

			throw new ResourceNotFoundException("Dear customer, your SIM is not ACTIVATED. Please try to Activate with shortCode 121 OR web!!");
		}

		Customer customer = customerRepository.findByMsisdn(msisdn)
				.orElseThrow(() -> new ResourceNotFoundException("customer not found with given msisdn !!"));

		if(customer.getPayment() == null && !"PAID".equals(customer.getPayment())) {
			throw new ResourceNotFoundException("ddear customer before recharging first pay the sim card payment !!");
		}
		
		if (customer.getServiceStatus().equals(Customer.Status.ACTIVE)) {

			customer.setMonthlyLimit(amount);

			//set nextInvoiceDate 
			customer.setNextInoviceDate(displayDateInfo(currentDate));

			customerRepository.save(customer);
		} else {
			throw new ResourceNotFoundException("pls contact to customer care,customer status is not Active !!");
		}

	}

	//date current date 14/oct < 14 save same month 14/oct  and current date  14 ya 17 >= 14 next month of 14/Nov
	private LocalDate displayDateInfo(LocalDate inputDate) {

		LocalDate currentDate = LocalDate.now();

		LocalDate day14ThisMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 14);
		
		LocalDate resultDate;
		
		if (inputDate.isBefore(day14ThisMonth)) {
			
			resultDate = day14ThisMonth;
			
		} else {
			
			resultDate = day14ThisMonth.plusMonths(1);
		}
		return resultDate;
		//System.out.println("The relevant date is: " + resultDate);
	}

	@Override
	public CustomerDto updateCustomerByField(int customerId, Map<String, Object> fields) {

		fields.forEach((k, v) -> {

			if (k.contains("msisdn") || k.contains("customerType") || k.contains("serviceStatus")
					|| k.contains("imageName") || k.contains("createDateTime") || k.contains("payment")
					|| k.contains("monthlyLimit") || k.contains("currentMonthlyAmount")) {
				
				throw new BadApiRequestException("some fields are not updatable, if you want to change it contact to Customer care !!");
			}
		});

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("customer not found with given id !!"));
		customer.setUpdationTime(LocalDateTime.now());

		if (fields == null || fields.isEmpty()) {
			throw new BadApiRequestException("request is required.... something went wrong !!");
		}
		try {

			fields.forEach((key, value) -> {

				Field field = ReflectionUtils.findField(Customer.class, key);
				field.setAccessible(true);
				ReflectionUtils.setField(field, customer, value);

			});
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new BadApiRequestException("invalid request !!");
		}
		customer.setUpdationTime(LocalDateTime.now());

		//customerRepository.save(customer);
		return mapper.map(customerRepository.save(customer), CustomerDto.class);
	}

	@Override
	public ImageResponse updateCustomerImage(int customerId, MultipartFile image) {

		if (image.isEmpty()) {

			throw new BadApiRequestException("Current request is not a multipart request !!");
		}
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("customer not found with given id !!"));

		//delete customer image
		//String currentImageName = customer.getImageName();

		if (customer.getImageName() != null && !customer.getImageName().isEmpty()) {

			//String fullPath = imagePath + File.separator + customer.getImageName();
			String fullPath = imagePath + customer.getImageName();
			System.out.println(fullPath);

			try {

				Path path = Paths.get(fullPath);
				Files.delete(path);
				//System.out.println("exist image deleted !!");
				logger.info("exist image deleted !!");

				//upload image
				try {
					String imageName = fileService.uploadFile(image, imagePath);

					customer.setImageName(imageName);

					customerRepository.save(customer);

					return ImageResponse.builder().imageName(imageName).message("image updated").success(true)
							.status(HttpStatus.OK).build();

				} catch (IOException e) {

					throw new BadApiRequestException("not update image :" + e.getMessage());
				}

			} catch (IOException e) {
				//System.out.println(e.getMessage());
				//logger.error("not delete exist image : ", e);
				throw new BadApiRequestException("not delete exist image :  " + e.getMessage());
			}
		} else {

			//upload image
			try {
				String imageName = fileService.uploadFile(image, imagePath);

				customer.setImageName(imageName);

				customerRepository.save(customer);

				return ImageResponse.builder().imageName(imageName).message("image updated").success(true)
						.status(HttpStatus.OK).build();

			} catch (IOException e) {

				throw new BadApiRequestException("not update image :" + e.getMessage());
			}
		}

	}

	@Override
	public ResponseMessage createErpCustomer(ErpCustomerRequestDto erpCustomerRequestDto) {
		
		//System.out.println("ERP request ...");
		logger.info("ERP request ...");
		
		final int accountId = 1;
		final int invoiceDeliveryId = 1;
		final int orderPeriodId = 1;
		final int partnerId = 22; //22 ERP
		final int baseUserId = 9; //9 ERP

		AccountType accountType = accountTypeRepository.findById(accountId)
				.orElseThrow(() -> new ResourceNotFoundException("account type not found with given id !!"));

		InvoiceDeliveryMethod invoiceDeliveryMethod = invoiceDeliveryMethodRepository.findById(invoiceDeliveryId)
				.orElseThrow(() -> new ResourceNotFoundException("invoice delevery method not found with given id !!"));
		
		Partner partner = partnerRepository.findById(partnerId)
				.orElseThrow(() -> new ResourceNotFoundException("partner not found with given id !!"));
		
		BaseUser baseUser = baseUserRepository.findById(baseUserId)
				.orElseThrow(() -> new ResourceNotFoundException("base user not found with given id !!"));
		
		OrderPeriod orderPeriod = orderPeriodRepository.findById(orderPeriodId)
				.orElseThrow(() -> new ResourceNotFoundException("order period not found with given id !!"));

		Customer customer = new Customer();
		customer.setAccountType(accountType);
		customer.setInvoiceDeliveryMethod(invoiceDeliveryMethod);
		customer.setPartner(partner);
		customer.setBaseUser(baseUser);
		customer.setOrderPeriod(orderPeriod);
		
		customer.setFirstName(erpCustomerRequestDto.getFirstName());
		
		List<Customer> customers = customerRepository.findByEmail(erpCustomerRequestDto.getEmail());
		if (!customers.isEmpty() ) {
			throw new ResourceNotFoundException("you have already registered !!");
		}
		customer.setLastName(erpCustomerRequestDto.getLastName());
		customer.setEmail(erpCustomerRequestDto.getEmail());
		customer.setDateOfBirth(erpCustomerRequestDto.getDateOfBirth());
		customer.setGender(erpCustomerRequestDto.getGender());
		customer.setMaritalStatus(erpCustomerRequestDto.getMaritalStatus());
		customer.setStreetAddres1(erpCustomerRequestDto.getStreetAddres1());
		customer.setStreetAddres2(erpCustomerRequestDto.getStreetAddres2());
		customer.setCity(erpCustomerRequestDto.getCity());
		customer.setStateProvince(erpCustomerRequestDto.getStateProvince());
		customer.setPostalCode(erpCustomerRequestDto.getPostalCode());
		customer.setCountryCode(erpCustomerRequestDto.getCountryCode());
		customer.setPersonTitle(erpCustomerRequestDto.getPersonTitle());
		customer.setPhoneCountryCode(erpCustomerRequestDto.getPhoneCountryCode());
		customer.setPhonePhoneNumber(erpCustomerRequestDto.getPhonePhoneNumber());
		customer.setAlternateNumber(erpCustomerRequestDto.getAlternateNumber());
		customer.setLandlineNumber(erpCustomerRequestDto.getLandlineNumber());
		//
		customer.setExcludeAging(0);
		customer.setOptlock(random.nextInt(999999999));
		customer.setUseParentPricing(false);
		customer.setCreateDateTime(LocalDateTime.now());
		customer.setDeleted(0);
		customer.setServiceType("mobility");
		//customer.setServiceStatus(Customer.Status.ACTIVE);
		
		
		
		try {
			
			Customer savedCustomer = customerRepository.save(customer);
			
			return ResponseMessage.builder().message("save successfully !!").id(savedCustomer.getId()).success(true).status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			return ResponseMessage.builder().message("some thing went wrong !! -> "+e.getMessage()).success(true).status(HttpStatus.BAD_REQUEST).build();
		}
	
	}

	@Override
	public void addChildSimWithParent(String parentMsisdn, Map<String, String> childNumbers) {
		
		/*Customer customer = customerRepository.findByMsisdn(parentMsisdn).orElseThrow( () -> new ResourceNotFoundException("customer not found with given msisdn !"));
		
		int customerId = customer.getId();
		
		if (childNumbers.isEmpty()) {
			throw new BadApiRequestException("request is required.... something went wrong !!");
		}*/
		
		
		
	}

	@Override
	@Transactional
	public ApiResponseMessage addChildNumberwithParnet(String parentMsisdn, String childMsisdn) {

			
			Customer parentCustomer = customerRepository.findByMsisdn(parentMsisdn).orElseThrow( () -> new ResourceNotFoundException("parent customer not found with given msisdn !"));

			System.out.println("P: Parent ID : " + parentCustomer.getParentId());
			System.out.println("P: Parent pricing :" + parentCustomer.getUseParentPricing());
			System.out.println("P: is Parent : " + parentCustomer.getIsParent());
			if (parentCustomer.getParentId() >0 && Boolean.TRUE.equals(parentCustomer.getUseParentPricing()) ) {
				
				throw new BadApiRequestException("P: you are also child account: "+parentMsisdn+ " !!");
			}
			if (!parentCustomer.getCustomerType().equalsIgnoreCase(POSTPAID)) {
				
				throw new BadApiRequestException("parent account msisdn"+"|"+parentCustomer.getSimInventory().getMsisdn()+"|"+ "not postpaid");
			}
			parentCustomer.setIsParent(1);
			customerRepository.save(parentCustomer);
			
			Customer childCustomer = customerRepository.findByMsisdn(childMsisdn).orElseThrow(() -> new ResourceNotFoundException("child customer not found with given msisdn !"));

			System.out.println("C: Parent ID : " + childCustomer.getParentId());
			System.out.println("C: Parent pricing :" + childCustomer.getUseParentPricing());
			System.out.println("C: is Parent : " + childCustomer.getIsParent());
			if (!childCustomer.getCustomerType().equalsIgnoreCase(POSTPAID)) {
				
				throw new BadApiRequestException("child account msisdn" + "|" + childCustomer.getSimInventory().getMsisdn() + "|" + "not postpaid");
			}
			if (childCustomer.getParentId() > 0 && Boolean.TRUE.equals(childCustomer.getUseParentPricing()) ) {

				throw new BadApiRequestException("C: you are also child account : "+childMsisdn+ " !!");
			}
			if (childCustomer.getIsParent() == 1) {
				
				throw new BadApiRequestException("you are also parent account : "+childMsisdn+ " !!");
			}

			//childCustomer.setIsParent(1);
			childCustomer.setUseParentPricing(true);
			childCustomer.setParentId(parentCustomer.getId());
			childCustomer.setChildCreationTime(LocalDateTime.now());
			childCustomer.setMonthlyLimit(null);
			childCustomer.setCurrentMonthlyAmount(null);
			childCustomer.setNextInoviceDate(null);
			
			customerRepository.save(childCustomer);
			
			return ApiResponseMessage.builder().message("child account added successfully !!").success(true).status(HttpStatus.OK).build();

	}

	@Override
	public List<CustomerDto> getAllChildAccountsOfParent(String msisdn) {
		
		Customer customer = customerRepository.findByMsisdn(msisdn).orElseThrow( () -> new ResourceNotFoundException("parent customer not found with given msisdn !"));

		if (!customer.getCustomerType().equalsIgnoreCase(POSTPAID)) {
			
			throw new BadApiRequestException("msisdn"+"|"+customer.getSimInventory().getMsisdn()+"|"+ "not postpaid");
		}
		
		//List<Customer> customers = customerRepository.findByParentId(customer.getId());
		return customerRepository.findByParentId(customer.getId()).stream().map( custmer -> mapper.map(custmer, CustomerDto.class)).collect(Collectors.toList());
	}

	@Override
	public ResponseMessage updateErpCustomer(int customerId, Map<String, Object> fields) {
		
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("customer not found with given id !!"));

		if (fields == null || fields.isEmpty()) {
			throw new BadApiRequestException("Invalid request !!");
		}

		fields.forEach((k, v) -> {

			if (k.contains("msisdn") || k.contains("customerType") || k.contains("serviceStatus")
					|| k.contains("imageName") || k.contains("createDateTime") || k.contains("payment")
					|| k.contains("monthlyLimit") || k.contains("currentMonthlyAmount")) {
				
				throw new BadApiRequestException("some fields are not updatable, if you want to change it contact to Customer care !!");
			}
		});

		fields.forEach((key, value) -> {
			Field field = ReflectionUtils.findField(Customer.class, key);
			if (field != null) {
				field.setAccessible(true);
				ReflectionUtils.setField(field, customer, value);
			}
		});

		// Update cust modification time
		customer.setUpdationTime(LocalDateTime.now());

	        try {
	        	
	            Customer updatedCustomer = customerRepository.save(customer);
	            
	            return ResponseMessage.builder()
	                    .message("Customer updated successfully!")
	                    .id(updatedCustomer.getId())
	                    .success(true)
	                    .status(HttpStatus.OK)
	                    .build();
	            
	        } catch (Exception e) {
	        	
	            return ResponseMessage.builder()
	                    .message("Something went wrong: " + e.getMessage())
	                    .success(false)
	                    .status(HttpStatus.BAD_REQUEST)
	                    .build();
	        }
	    }

}
