package com.wpits.services.serviceImpls;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.InvoiceItemDTO;
import com.wpits.dtos.PaymentDto;
import com.wpits.dtos.RequestToCrmAccountForPackAssign;
import com.wpits.dtos.ServiceRequest;
import com.wpits.entities.Cart;
import com.wpits.entities.CreditCard;
import com.wpits.entities.Currency;
import com.wpits.entities.Customer;
import com.wpits.entities.CustomerDeviceInfo;
import com.wpits.entities.CustomerInvoice;
import com.wpits.entities.DeviceInventory;
import com.wpits.entities.Invoice;
import com.wpits.entities.Order;
import com.wpits.entities.OrderItem;
import com.wpits.entities.Payment;
import com.wpits.entities.PaymentMethod;
import com.wpits.entities.PaymentResult;
import com.wpits.entities.RouterInventory;
import com.wpits.entities.SimInventory;
import com.wpits.exceptions.BadApiRequestException;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.CartRepository;
import com.wpits.repositories.CreditCardRepository;
import com.wpits.repositories.CurrencyRepository;
import com.wpits.repositories.CustomerDeviceInfoRepository;
import com.wpits.repositories.CustomerInvoiceRepository;
import com.wpits.repositories.CustomerRepository;
import com.wpits.repositories.DeviceInventoryRepository;
import com.wpits.repositories.InvoiceRepository;
import com.wpits.repositories.OrderRepository;
import com.wpits.repositories.PaymentMethodRepository;
import com.wpits.repositories.PaymentRepository;
import com.wpits.repositories.PaymentResultRepository;
import com.wpits.repositories.RouterInventoryRepository;
import com.wpits.repositories.SimInventoryRepository;
import com.wpits.services.PaymentService;

import reactor.core.publisher.Mono;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private PaymentResultRepository paymentResultRepository;

	@Autowired
	private PaymentMethodRepository paymentMethodRepository;

	@Autowired
	private CreditCardRepository creditCardRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private DeviceInventoryRepository deviceInventoryRepository;

	@Autowired
	private SimInventoryRepository simInventoryRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private CustomerInvoiceRepository customerInvoiceRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CustomerDeviceInfoRepository customerDeviceInfoRepository;

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private RouterInventoryRepository routerInventoryRepository;

	@Value("${udr.server.url}")
	private String udrUrl;

	@Value("${hss.provisioning.server.url}")
	String provisionUrl;

	@Value("${abmf.prepaid.plan.server.url}")
	String prepaidPlanUrl;

	@Value("${abmf.postpaid.plan.server.url}")
	String postpaidPlanUrl;

	@Value("${abmf.server.url}")
	String updateAbmfPostpaidStatusURL;
	
	@Value("${router.info.abmf.url}")
	String shareRouterInfoAbmfURL;

	private Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Override
	@Transactional
	public ApiResponseMessage createPayment(PaymentDto paymentDto, int currencyId, int paymentResultId,
			int paymentMethodId, int creditCardId, String msisdn) {

		String createProfileInPcfUrl = udrUrl + "create-profile";
		String changeProfileInPcfURL = udrUrl + "change-profile";

		Payment savedPayment = null;
		//System.out.println(creditCardId);

		Currency currency = currencyRepository.findById(currencyId).orElseThrow(() -> new ResourceNotFoundException("currency not found with given id !!"));
		PaymentResult paymentResult = paymentResultRepository.findById(paymentResultId).orElseThrow(() -> new ResourceNotFoundException("payment result not found with given id !!"));
		PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElseThrow(() -> new ResourceNotFoundException("payment method not found with given id !!"));
		//CreditCard creditCard = creditCardRepository.findById(creditCardId).orElseThrow(() -> new ResourceNotFoundException("credit card not found with given id !!"));

		Payment payment = mapper.map(paymentDto, Payment.class);
		payment.setCurrency(currency);
		payment.setPaymentResult(paymentResult);
		payment.setPaymentMethod(paymentMethod);
		//payment.setCreditCard(creditCard);
		payment.setCreateDatetime(LocalDateTime.now());
		// payment.setUpdateDatetime(LocalDateTime.now().plusDays(29));
		payment.setPaymentDate(LocalDate.now());
		payment.setOptlock(ThreadLocalRandom.current().nextInt(999999));
		//payment.setMsisdns(msisdns);
		//payment.setDeviceIds(devices);
		//payment.setMode("Credit Card");
		payment.setMode(creditCardId >= 1 ? "Credit Card" : "Cash");
		//payment .setAmount(totalAmount);

		if (creditCardId >= 1) {

			CreditCard creditCard = creditCardRepository.findById(creditCardId).orElseThrow(() -> new ResourceNotFoundException("credit card not found with given id !!"));
			payment.setCreditCard(creditCard);
		}

		savedPayment = paymentRepository.save(payment);

		/* PaymentDto paymentInfo = mapper.map(savedPayment, PaymentDto.class); */

		if (paymentDto.getCustInvoiceId() > 0) {

			CustomerInvoice customerInvoice = customerInvoiceRepository.findById(paymentDto.getCustInvoiceId()).orElseThrow(() -> new ResourceNotFoundException("customer invoice not found with given id !!"));

			//String orderNumber = customerInvoice.getOrderNumber();
			if (customerInvoice.getOrderNumber() == null) {
				throw new ResourceNotFoundException("order number not found with give customerInvoice !!");
			}

			int customerId = 0;
			int partnerId = 0;

			Order order = orderRepository.findByOrderNumber(customerInvoice.getOrderNumber()).orElseThrow(() -> new ResourceNotFoundException("order not found with given order number !!"));

			partnerId = order.getPartnerId();
			String token = order.getToken();

			List<Customer> customers = customerRepository.findByEkycToken(token);
			System.out.println("total customer product paid and unpaid " + customers.size());

			if (customers.isEmpty()) {
				throw new ResourceNotFoundException("Customer not found with given token !!");
			}

			Customer cust = customers.get(0);
			customerId = cust.getId();

			//Order order = orderRepository.findByOrderNumber(customerInvoice.getOrderNumber()).orElseThrow(() -> new ResourceNotFoundException("order not found with given order number !!"));

			double totalAmount = customerInvoice.getTotalAmount();
			double amount = paymentDto.getAmount();

			if (amount != totalAmount) {
				throw new ResourceNotFoundException("amount incorrect pls check again !!");
			}

			order.setPaymentStatus(true);
			orderRepository.save(order);

			//save to CRM
			//msisdn map

			//sim AllocationDate set

			try {

				//Customer customer = null;
				//List<InvoiceItemDTO> items = customerInvoice.getDetails();
				List<InvoiceItemDTO> items = objectMapper.readValue(customerInvoice.getDetails(),new TypeReference<List<InvoiceItemDTO>>() {});

				/*List<OrderItem> orderItems = order.getOrderItems();
				for (OrderItem item : orderItems) {*/
				//items.forEach(System.out::println);

				for (InvoiceItemDTO item : items) {

					if (item.getProductType().toLowerCase().contains("sim") && item.getNumber() != null) {

						String msisdnNumber = item.getNumber();

						Customer customer = customerRepository.findByMsisdn(msisdnNumber).orElseThrow(() -> new ResourceNotFoundException("customer not found with given msisdn"));
						customer.setPayment("PAID");
						customerRepository.save(customer);

						//sim  provision in HSS
						simProvision(customer);

						//profile create in PCF
						customerProfileInPCF(customer, createProfileInPcfUrl);

						//share ABMF
						shareToABMF(customer, paymentDto, savedPayment);

					}
					if (item.getProductSerialNo() > 0) {

						DeviceInventory deviceInventory = deviceInventoryRepository.findById(item.getProductSerialNo()).orElse(null);

						if (deviceInventory != null) {

							if (deviceInventory.getAllocationDate() != null) {
								//order item delete krke dekhta hu yha jo paid ho gya kisi aur se
								throw new ResourceNotFoundException("this device product already sold pls go to cart to add new product and generate the order !!");

							}

							CustomerDeviceInfo customerDeviceInfo = new CustomerDeviceInfo();
							customerDeviceInfo.setStatus("PAID");
							customerDeviceInfo.setOrderId(order.getId());
							customerDeviceInfo.setInvoiceId(paymentDto.getCustInvoiceId());
							customerDeviceInfo.setPaymentId(paymentDto.getId());
							customerDeviceInfo.setDeviceId(deviceInventory.getId());
							customerDeviceInfo.setCustomerId(customerId);
							customerDeviceInfo.setPartnerId(partnerId);
							customerDeviceInfo.setPaymentId(savedPayment.getId());
							customerDeviceInfo.setPurchaseDate(LocalDate.now());

							customerDeviceInfoRepository.save(customerDeviceInfo);

							deviceInventory.setAllocationDate(LocalDate.now());
							deviceInventoryRepository.save(deviceInventory);

							//Cart ko clear/delete krna hai
							List<Cart> carts = cartRepository.findByDeviceId(deviceInventory.getId());

							cartRepository.deleteAll(carts);
						}else {

							RouterInventory routerInventory = routerInventoryRepository.findBySerialNumber(item.getProductSerialNo()).orElse(null);

							System.out.println("@@@Router serial No :" + item.getProductSerialNo());

							if (routerInventory != null) {

								System.out.println("###Router serial No :" + routerInventory.getSerialNumber());
								if (routerInventory.getAllocationDate() != null) {

									throw new ResourceNotFoundException("this router product already sold pls go to cart to add new product and generate the order !!");

								}
								
								Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("customer not fount with given id !!"));							
								
								//simInventory && deviceInventory == null  insert router to same customer like EKYC with temporary info
								if (customer.getSimInventory() == null && customer.getDeviceInventory() == null && customer.getRouterInventory() == null) {
									
									customer.setRouterInventory(routerInventory);
									customer.setCustomerType("Broadband");
									customer.setPayment("PAID");
									customerRepository.save(customer);

									routerInventory.setAllocationDate(LocalDateTime.now());

									//ABMF
									shareRouterInfoToABMF(routerInventory);

									routerInventory.setActivationDate(LocalDateTime.now());
									routerInventoryRepository.save(routerInventory);
									
								}  else if (customer.getRouterInventory() != null && customer.getSimInventory() == null && customer.getDeviceInventory() == null) {
									//Direct CRM se Router Millega OR null 
									customer.setRouterInventory(routerInventory);
									customer.setPayment("PAID");
									customerRepository.save(customer);

									routerInventory.setAllocationDate(LocalDateTime.now());

									//ABMF
									shareRouterInfoToABMF(routerInventory);

									routerInventory.setActivationDate(LocalDateTime.now());
									routerInventoryRepository.save(routerInventory);
									
								} else {
									//new customer to router with given token EKYC
									createCustomer(customer,routerInventory);	
									
									routerInventory.setAllocationDate(LocalDateTime.now());

									//ABMF
									shareRouterInfoToABMF(routerInventory);

									routerInventory.setActivationDate(LocalDateTime.now());
									routerInventoryRepository.save(routerInventory);
								}
									

									//Cart ko clear/delete krna hai
									List<Cart> carts = cartRepository.findByRouterSerialNo(routerInventory.getSerialNumber());

									cartRepository.deleteAll(carts);

							}

						}

						/*}*/
					}
				}

			} catch (Exception e) {

				throw new ResourceNotFoundException("Failed to parse invoice details" + e.getMessage());
			}

			//prepaid plan recharge payment yha se 
		} else if (paymentDto.getProduct().equalsIgnoreCase("plan")) {

			System.out.println("---------- PREPAID Plan / Pack-------");
			//chk sim status
			SimInventory simInventory = simInventoryRepository.findByMsisdn(msisdn).orElseThrow(() -> new ResourceNotFoundException("sim not found with given msisdn !!"));

			if (simInventory.getActivationStatus() == null || !simInventory.getActivationStatus().equalsIgnoreCase("ACTIVATE")) {

				throw new ResourceNotFoundException("Dear customer, your SIM is not ACTIVATED. Please try to Activate with shortCode 121 OR web!!");
			}

			// verify msisdn
			Customer customer = customerRepository.findByMsisdn(msisdn).orElseThrow(() -> new ResourceNotFoundException("customer's msisdn not found with given number !!"));

			if (customer.getPayment() == null && !"PAID".equals(customer.getPayment())) {
				throw new ResourceNotFoundException("ddear customer before recharging first pay the sim card payment !!");
			}

			if (customer.getCustomerType().equalsIgnoreCase("postpaid")) {

				throw new ResourceNotFoundException("it's a postpaid user.this service not for postpaid !!");
			}
			//pcf
			customerProfileInPCF(customer, changeProfileInPcfURL);

			//ABMF
			shareToABMF(customer, paymentDto, savedPayment);

		} else {
			throw new ResourceNotFoundException("pls select any product !!");
		}

		if (savedPayment != null) {
			return ApiResponseMessage.builder().message("Payment processed successfully !!").success(true).status(HttpStatus.OK).build();

		} else {
			return ApiResponseMessage.builder().message("Payment Failed !!").success(false).status(HttpStatus.BAD_REQUEST).build();

		}
		// return paymentInfo;
	}

	private void createCustomer(Customer customer, RouterInventory routerInventory) {
		
		System.out.println("create new customer to router assign !!");
		
		Customer customr = new Customer();
		
		customr.setAccountType(customer.getAccountType());
		customr.setBaseUser(customer.getBaseUser());
		customr.setOrderPeriod(customer.getOrderPeriod());
		customr.setInvoiceDeliveryMethod(customer.getInvoiceDeliveryMethod());
		customr.setPartner(customer.getPartner());
		
		//customr.setReferralFeePaid(customer.getReferralFeePaid());
		//customr.setAutoPaymentType(customer.getAutoPaymentType());
		//customr.setDueDateUnitId(customer.getDueDateUnitId());
		//customr.setDueDateValue(customer.getDueDateValue());
		//customr.setDfFm(customer.getDfFm());
		//customr.setParentId(customer.getParentId());
		//customr.setIsParent(customer.getIsParent());
		customr.setExcludeAging(customer.getExcludeAging());
		//customr.setInvoiceChild(customer.getInvoiceChild());
		customr.setOptlock(customer.getOptlock());
		//customr.setDynamicBalance(customer.getDynamicBalance());
		//customr.setCreditLimit(customer.getCreditLimit());
		//customr.setAutoRecharge(customer.getAutoRecharge());
		customr.setUseParentPricing(false);
		//customr.setNextInvoiceDayOfPeriod(customer.getNextInvoiceDayOfPeriod());
		//customr.setNextInoviceDate(customer.getNextInoviceDate());
		//customr.setInvoiceDesign(customer.getInvoiceDesign());
		//customr.setCreditNotificationLimit1(customer.getCreditNotificationLimit1());
		//customr.setCreditNotificationLimit2(customer.getCreditNotificationLimit2());
		//customr.setRechargeThreshold(customer.getRechargeThreshold());
		//customr.setMonthlyLimit(customer.getMonthlyLimit());
		//customr.setCurrentMonthlyAmount(customer.getCurrentMonthlyAmount());
		customr.setCurrentMonth(LocalDateTime.now());
		customr.setOrganizationName(customer.getOrganizationName());
		customr.setStreetAddres1(customer.getStreetAddres1());
		customr.setStreetAddres2(customer.getStreetAddres2());
		customr.setCity(customer.getCity());
		customr.setStateProvince(customer.getStateProvince());
		customr.setPostalCode(customer.getPostalCode());
		customr.setCountryCode(customer.getCountryCode());
		customr.setLastName(customer.getLastName());
		customr.setFirstName(customer.getFirstName());
		customr.setPersonInitial(customer.getPersonInitial());
		customr.setPersonTitle(customer.getPersonTitle());
		customr.setPhoneCountryCode(customer.getPhoneCountryCode());
		customr.setPhoneAreaCode(customer.getPhoneAreaCode());
		customr.setPhonePhoneNumber(customer.getSimInventory().getMsisdn());
		customr.setFaxCountryCode(customer.getFaxCountryCode());
		customr.setFaxAreaCode(customer.getFaxAreaCode());
		customr.setFaxPhoneNumber(customer.getFaxPhoneNumber());
		customr.setEmail(customer.getEmail());
		customr.setCreateDateTime(LocalDateTime.now());
		customr.setDeleted(customer.getDeleted());
		customr.setNotificationInclude(customer.getNotificationInclude());
		customr.setCustomerType("Broadband");
		customr.setGender(customer.getGender());
		customr.setEkycStatus(customer.getEkycStatus());
		customr.setEkycToken(customer.getEkycToken());
		customr.setEkycDate(LocalDateTime.now());
		customr.setAlternateNumber(customer.getAlternateNumber());
		customr.setLandlineNumber(customer.getLandlineNumber());
		customr.setDateOfBirth(customer.getDateOfBirth());
		customr.setVatId(customer.getVatId());
		customr.setProfession(customer.getProfession());
		customr.setMaritalStatus(customer.getMaritalStatus());
		customr.setImageName(customer.getImageName());
		customr.setServiceStatus(Customer.Status.ACTIVE);
		customr.setServiceType("FTTH");
		customr.setRouterInventory(routerInventory);
		customr.setPayment("PAID");
		
		customerRepository.save(customr);

		
	}

	//HSS provision
	private void simProvision(Customer customer) {

		try {

			System.out.println("************** UDM/HSS PROVISIONING *******************************");
			// String loginUrl = "";
			//String provisionUrl = "http://172.17.1.11:9697/api/hss/detail/save/subscriber";

			/*RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer e84e25f9-1613-4e55-957e-7a683ad3bb54");*/

			/*Random r = new Random();
			int num = r.nextInt(999999999);
			char c = (char) (r.nextInt(26) + 'A');
			String orderId = num + "" + c + "";
			System.out.println(orderId);*/

			String token = UUID.randomUUID().toString();

			String orderId = ThreadLocalRandom.current().nextInt(999999999) + "";

			String requestBody = "{ \"orderId\": \"" + orderId + "\", \"imsi\": \""
					+ customer.getSimInventory().getImsi() + "\", \"msisdn\": \""
					+ customer.getSimInventory().getMsisdn() + "\","
					+ " \"serviceCapability\": { \"Attach\": { \"LTE\": true, \"NR\": true, \"IMS\": true },"
					+ " \"Voice\": { \"Outgoing\": true, \"Incoming\": true },"
					+ " \"SMS\": { \"OutgoingSms\": true, \"OutgoingServiceSms\": true, \"IncomingSms\": true },"
					+ " \"DataService\": { \"LTE\": true, \"NR\": true } } }";
			//System.out.println("@@@@@@@@" + requestBody);
			logger.info("request {}", requestBody);

			/*HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			
			ResponseEntity<String> response = restTemplate.postForEntity(provisionUrl, request, String.class);
			
			logger.info("UDM / HSS Provisioning :{} ",response);*/

			WebClient webClient = WebClient.create();
			String response = webClient.post().uri(provisionUrl).contentType(MediaType.APPLICATION_JSON)
					.header("authorization", "Bearer " + token).bodyValue(requestBody).retrieve()
					.bodyToMono(String.class).block();

			logger.info("UDM / HSS Provisioning :{} ", response);

			// activation date update in SIM when provisioning status 200 ok
			simInventoryRepository.updateActivationDate(LocalDateTime.now(), customer.getSimInventory().getMsisdn());
		} catch (Exception e) {
			logger.error("error {}", e.getMessage());
		}
	}

	//PCF /UDR profile send
	private void customerProfileInPCF(Customer customer, String url) {

		try {
			System.out.println("################## PCF/UDR Service ###########################");

			logger.info("This URL hit : {}", url);

			WebClient webClient = WebClient.create();

			Map<String, String> udrRequest = new HashMap<>();
			udrRequest.put("supi", customer.getSimInventory().getImsi());
			udrRequest.put("subscriberProfile", url.contains("create") ? "Redirect" : "HS_DATA_IMS");

			String response = (url.contains("create") ? webClient.post() : webClient.put()).uri(url)
					.contentType(MediaType.APPLICATION_JSON).bodyValue(udrRequest).retrieve().bodyToMono(String.class)
					.block();

			//System.out.println(response);

			if (url.contains("create")) {

				logger.info("PCF/UDR create profile response: {}", response);
			} else {

				logger.info("PCF/UDR change profile response: {}", response);
			}

		} catch (Exception e) {
			logger.error("Error from PCF/UDR: {}", e.getMessage());
		}

		/*try {
			System.out.println("################## PCF/UDR Service ###########################");
		
			//String URL = "http://172.17.1.22:18780/api/udr/subscriber/create-profile";
		
			System.out.println("@@@@@@@@@@@@@" + url);*/

		/*RestTemplate template = new RestTemplate();
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);*/

		/*if (url.contains("create")) {
		
			logger.info("this URL hit : {}", url);
			Map<String, String> udrRequest = new HashMap<>();
			udrRequest.put("supi", customer.getSimInventory().getImsi());
			udrRequest.put("subscriberProfile", "Redirect");
		
			HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(udrRequest, httpHeaders);
			
			ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
			
			
			WebClient webClient = WebClient.create();
			Mono<String> response = webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON)
					.bodyValue(udrRequest).retrieve().bodyToMono(String.class);
			logger.info("PCF/UDR create profile :{} ", response);
		}*/ /*else {
					
					logger.info("this URL hit : {}", url);
					Map<String, String> udrRequest = new HashMap<>();
					udrRequest.put("supi", customer.getSimInventory().getImsi());
					udrRequest.put("subscriberProfile", "HS_DATA_IMS");
					
					HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(udrRequest,httpHeaders);
					
					ResponseEntity<String> response = template.exchange(url, HttpMethod.PUT,requestEntity, String.class);
					
					
					WebClient webClient = WebClient.create();
					Mono<String> response = webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON)
							.bodyValue(udrRequest).retrieve().bodyToMono(String.class);
					logger.info("PCF/ UDR change profie : {} ", response);
					}*/

		/*} catch (Exception e) {
			logger.error("error from PCF/UDR :{}", e.getMessage());
		}*/
	}

	//share to ABMF

	private void shareToABMF(Customer customer, PaymentDto paymentDto, Payment savedPayment) {

		try {
			System.out.println("############################ Share To ABMF  ############################");

			//String url = "http://172.5.10.2:9696/api/customer/reports/save";

			/*RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);*/

			RequestToCrmAccountForPackAssign request = new RequestToCrmAccountForPackAssign();

			/*if (paymentDto.getPlanId() > 0) {*/

			//System.out.println("############################ ABMF To PrePaid ############################");

			logger.info("with plan id {}", paymentDto.getPlanId());
			request.setFirst_name(customer.getFirstName());
			request.setLast_name(customer.getLastName());
			request.setEkyc_status(customer.getEkycStatus());
			request.setEkyc_token(customer.getEkycToken());
			logger.info("ekyc date {}", customer.getEkycDate());
			request.setEkyc_date(customer.getEkycDate());
			request.setCustomer_id(customer.getId());
			System.out.println("CustomerId : " + customer.getId());
			request.setCustomer_type(customer.getCustomerType());
			request.setMsisdn(customer.getSimInventory().getMsisdn());
			request.setImsi(customer.getSimInventory().getImsi());
			request.setPayment_status(paymentDto.getPaymentStatus());
			request.setParent_customer_id(customer.getParentId());

			request.setPack_id(paymentDto.getPlanId() > 0 ? paymentDto.getPlanId() : 0);

			/*HttpEntity<RequestToCrmAccountForPackAssign> requestEntity1 = new HttpEntity<>(request, headers);
			
			ResponseEntity<String> response = restTemplate.exchange(prepaidPlanUrl, HttpMethod.POST, requestEntity1,String.class);
			
			*/

			logger.info("ABMF request : {}", request);
			logger.info("pack_id : {}", request.getPack_id());
			WebClient webClient = WebClient.create();
			String response = webClient.post().uri(prepaidPlanUrl).contentType(MediaType.APPLICATION_JSON)
					.bodyValue(request).retrieve().bodyToMono(String.class).block();

			//String response = responseAbmf.block();

			logger.info("ABMF response: {}", response);

			if (response != null && paymentDto.getPlanId() > 0) {

				JSONObject jsonResponse = new JSONObject(response);

				if (jsonResponse.has("pack_name")) {

					String plan = jsonResponse.getString("pack_name");
					logger.info("Pack Name: {}", plan);

					savedPayment.setPlan(plan);
					savedPayment.setMsisdn(customer.getSimInventory().getMsisdn());
					savedPayment.setCustomerId(customer.getId());
					paymentRepository.save(savedPayment);
				}
			}
			/*}*/

		} catch (Exception e) {

			logger.error("error from ABMF :{}", e.getMessage());
		}
	}

	//Router share to ABMF

	private void shareRouterInfoToABMF(RouterInventory routerInventory) {

		try {
			System.out.println("################## Router info share to ABMF ###########################");

			logger.info("This URL hit : {}", shareRouterInfoAbmfURL);

			Map<String, Object> request = new HashMap<>();
			request.put("account_id", routerInventory.getId());
			request.put("create_date", routerInventory.getCreationTime());
			request.put("type", routerInventory.getType());
			request.put("serial_number", routerInventory.getSerialNumber());
			request.put("brand", routerInventory.getBrand());
			request.put("device_manufactorer", routerInventory.getDeviceManufactorer());
			request.put("vendor_id", routerInventory.getVendorId());
			request.put("ss_id", routerInventory.getSsId());
			request.put("cpe_config_url", routerInventory.getCpeConfigUrl());
			request.put("cpe_username", routerInventory.getCpeUsername());
			request.put("cpe_password", routerInventory.getCpePassword());
			request.put("allocation_date", LocalDateTime.now());
			request.put("activation_date", LocalDateTime.now());
			request.put("device_id", 0);
			request.put("plan_id", 0);
			request.put("device_status", routerInventory.getDeviceStatus());
			request.put("device_static_ip", routerInventory.getDeviceStaticIp());
			request.put("mac_address", routerInventory.getMacAddress());

			logger.info("Router request to ABMF : {}", request);
			WebClient webClient = WebClient.create();
			String response = webClient.post().uri(shareRouterInfoAbmfURL).contentType(MediaType.APPLICATION_JSON)
					.bodyValue(request).retrieve().bodyToMono(String.class).block();

			//String response = responseAbmf.block();

			logger.info("ABMF to Router response: {}", response);

		} catch (Exception e) {
			logger.error("Error from PCF/UDR: {}", e.getMessage());
		}

	}

	@Override
	public PaymentDto updatepayment(PaymentDto paymentDto, int paymentId) {

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new ResourceNotFoundException("payment not found with given id !!"));

		payment.setAttempt(paymentDto.getAttempt());
		payment.setAmount(paymentDto.getAmount());
		payment.setCreateDatetime(LocalDateTime.now());
		payment.setUpdateDatetime(LocalDateTime.now().plusDays(7));
		payment.setPaymentDate(LocalDate.now());
		payment.setDeleted(paymentDto.getDeleted());
		payment.setIsRefund(paymentDto.getIsRefund());
		payment.setIsPreauth(paymentDto.getIsPreauth());
		payment.setPayoutId(paymentDto.getPayoutId());
		payment.setBalance(paymentDto.getBalance());
		payment.setOptlock(paymentDto.getOptlock());
		payment.setPaymentPeriod(paymentDto.getPaymentPeriod());
		payment.setPaymentNotes(paymentDto.getPaymentNotes());
		payment.setPaymentStatus(paymentDto.getPaymentStatus());

		Payment savedPayment = paymentRepository.save(payment);

		return mapper.map(savedPayment, PaymentDto.class);
	}

	@Override
	public List<PaymentDto> getAllPayment() {
		return paymentRepository.findAll().stream().map(payment -> mapper.map(payment, PaymentDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public PaymentDto getByIdPayment(int paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new ResourceNotFoundException("payment not found  with given id !!"));
		return mapper.map(payment, PaymentDto.class);
	}

	@Override
	public void deletePayment(int paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new ResourceNotFoundException("payment not found with given id !!"));
		paymentRepository.delete(payment);
	}

	@Override
	public PaymentDto getPaymentRecordByCustId(int custId) {
		Payment payment = paymentRepository.findByCustomerId(custId)
				.orElseThrow(() -> new ResourceNotFoundException("payment not found with given cust id !!"));

		return mapper.map(payment, PaymentDto.class);
	}

	@Override
	@Transactional
	public ApiResponseMessage postPaidBillPayment(String invoiceNumber, Double amount, int currencyId, int paymentResultId,
			int paymentMethodId, int creditCardId, int partnerId, String transactionId) {

		logger.info("@@@ invoice number :{} ", invoiceNumber);

		Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
				.orElseThrow(() -> new ResourceNotFoundException("invoice not found with given invoiceNumber !!"));

		Customer customer = customerRepository.findById(invoice.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("customer not found with given id !!"));

		Currency currency = currencyRepository.findById(currencyId)
				.orElseThrow(() -> new ResourceNotFoundException("currency not found with given id !!"));

		PaymentResult paymentResult = paymentResultRepository.findById(paymentResultId)
				.orElseThrow(() -> new ResourceNotFoundException("payment result not found with given id !!"));

		PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
				.orElseThrow(() -> new ResourceNotFoundException("payment method not found with given id !!"));

		Payment payment = new Payment();

		payment.setCurrency(currency);
		payment.setPaymentResult(paymentResult);
		payment.setPaymentMethod(paymentMethod);

		if (creditCardId >= 1) {
			CreditCard creditCard = creditCardRepository.findById(creditCardId)
					.orElseThrow(() -> new ResourceNotFoundException("credit card not found with given id !!"));

			payment.setCreditCard(creditCard);
			//payment.setMode("Credit Card");

			payment.setTransactionId(transactionId);
		}

		if (invoice.getStatus() == Invoice.Status.PAID) {

			throw new ResourceNotFoundException("Invoice already paid");
		}
		if (invoice.getTotal().equals(amount)) {

			invoice.setStatus(Invoice.Status.PAID);

			invoiceRepository.save(invoice);

			if (customer.getServiceStatus() != Customer.Status.ACTIVE) {

				customer.setServiceStatus(Customer.Status.ACTIVE);

				//nextInvoice Date increase 1 month 
				//customer.setNextInoviceDate(customer.getNextInoviceDate().plusMonths(1));   this logic invoice genrate wale mai lga diya

				customerRepository.save(customer);
			}
		} else {

			throw new ResourceNotFoundException("Incorrect payment amount");
		}

		payment.setUserId(customer.getBaseUser().getId());
		payment.setCustomerId(customer.getId());
		payment.setMsisdn(customer.getSimInventory().getMsisdn());
		payment.setAttempt(0);
		payment.setAmount(amount);
		payment.setCreateDatetime(LocalDateTime.now());
		payment.setUpdateDatetime(LocalDateTime.now());
		payment.setPaymentDate(LocalDate.now());
		payment.setDeleted(0);
		payment.setIsRefund(0);
		payment.setIsPreauth(0);
		payment.setPayoutId(0);
		payment.setBalance(0.0);
		payment.setProduct("Post-Paid Bill Payment");
		payment.setPaymentStatus(true);
		payment.setOptlock(ThreadLocalRandom.current().nextInt(999999));
		payment.setCustomerType(customer.getCustomerType());
		payment.setMode(creditCardId >= 1 ? "Credit Card" : "Cash");
		payment.setPartnerId(partnerId);

		try {

			Payment savedPayment = paymentRepository.save(payment);

			Customer custmer = customerRepository.findById(savedPayment.getCustomerId())
					.orElseThrow(() -> new ResourceNotFoundException("customer not found with given id !!"));

			//ABMF
			updateStatusABMFofPostPaid(custmer);

			return ApiResponseMessage.builder().message("Payment processed successfully !!").success(true)
					.status(HttpStatus.OK).build();

		} catch (Exception e) {

			return ApiResponseMessage.builder().message("Payment Failed !!").success(false)
					.status(HttpStatus.BAD_REQUEST).build();
		}

	}

	//ABMF
	private void updateStatusABMFofPostPaid(Customer custmer) {

		try {

			logger.info("URL ABMF :{}", updateAbmfPostpaidStatusURL);
			/*RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);*/

			ServiceRequest request = new ServiceRequest();
			request.setCustomer_id(custmer.getId());
			request.setUser_status(custmer.getServiceStatus() + "");

			/*HttpEntity<ServiceRequest> requestEntity = new HttpEntity<>(request, headers);
			
			ResponseEntity<String> response = restTemplate.exchange(updateAbmfPostpaidStatusURL, HttpMethod.PUT,
					requestEntity, String.class);*/
			
			WebClient webClient = WebClient.create();
			
			String response = webClient.put()
					.uri(updateAbmfPostpaidStatusURL)
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(request)
					.retrieve()
					.bodyToMono(String.class)
					.block();

			logger.info("Response : {}", response);

		} catch (Exception e) {

			logger.error("ABMF URL :{} ", e.getMessage());
		}
	}

	@Override
	public void updatePostpaidAmountWithLateCharge() {
		
		System.out.println("postpaid late charge handler !!");
		
		List<Invoice> invoices = invoiceRepository.findByStatusAndIsVip(Invoice.Status.UNPAID, false);
		
		//invoices.stream().forEach(System.out::println);
		
		for (Invoice invoice : invoices) {
			
			//System.out.println(invoice.getMsisdn());
			
			if (LocalDate.now().isAfter(invoice.getDueDate()) && invoice.getTotal().compareTo(invoice.getBillAmount()) == 0) {
				
				System.out.println("check payable postpaid amount" + invoice.getMsisdn());
				
				invoice.setLateFee(5.0);
				
				invoice.setTotal(Double.sum(invoice.getBillAmount() , invoice.getLateFee()));
				
				invoiceRepository.save(invoice);
			}
		}
		
		/*if( invoice.getTotal().compareTo(invoice.getBillAmount()) == 0 ) {
			System.out.println("balance equal");
		}
		
		if(!invoice.isVip()) {
			
			System.out.println("hiiiii");
		}*/
	}

}
