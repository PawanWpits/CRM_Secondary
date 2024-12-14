package com.wpits.services.serviceImpls;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.hibernate.dialect.MariaDBDialect;
import org.modelmapper.ModelMapper;
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
import org.springframework.web.multipart.MultipartFile;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.PartnerSimResponse;
import com.wpits.dtos.SimInventoryDto;
import com.wpits.entities.Customer;
import com.wpits.entities.DeviceInventory;
import com.wpits.entities.PartnerPayment;
import com.wpits.entities.PartnerPayment.Status;
import com.wpits.entities.RouterInventory;
import com.wpits.entities.SimInventory;
import com.wpits.entities.Vendor;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.helpers.ExcelHelper;
import com.wpits.repositories.CustomerRepository;
import com.wpits.repositories.DeviceInventoryRepository;
import com.wpits.repositories.PartnerPaymentsRepository;
import com.wpits.repositories.RouterInventoryRepository;
import com.wpits.repositories.SimInventoryRepository;
import com.wpits.repositories.VendorRepository;
import com.wpits.services.SimInventoryService;

@Service
public class SimInventoryServiceImpl implements SimInventoryService {

	
	@Autowired
	private SimInventoryRepository simInventoryRepository;
	
	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private PartnerPaymentsRepository partnerPaymentsRepository;
	
	@Autowired
	private DeviceInventoryRepository deviceInventoryRepository;
	
	@Autowired
	private RouterInventoryRepository routerInventoryRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Value("${udr.server.url}")
	private String udrUrl;
	
	@Value("${abmf.server.url}")
	private String abmfChangeStatusUrl;
	
	@Value("${hss.server.url}")
	private String UdmChangeStatusUrl;

	@Override
	public SimInventoryDto createSimInventory(SimInventoryDto simInventoryDto) {
		Vendor vendor = vendorRepository.findById(simInventoryDto.getVendorId()).orElseThrow( () -> new ResourceNotFoundException( "vendor not found with given id !!"));
		SimInventory simInventory = mapper.map(simInventoryDto, SimInventory.class);
		simInventory.setBatchDate(LocalDate.now());
		//simInventory.setAllocationDate(LocalDateTime.now());
		//simInventory.setActivationDate(LocalDateTime.now());

		return mapper.map(simInventoryRepository.save(simInventory), SimInventoryDto.class);
	}

	@Override
	public SimInventoryDto updateSimInventory(SimInventoryDto simInventoryDto, int simInventoryId) {
		SimInventory simInventory = simInventoryRepository.findById(simInventoryId).orElseThrow(() -> new ResourceNotFoundException("sim inventory not found with given id !!"));
		simInventory.setMsisdn(simInventory.getMsisdn());
		simInventory.setCategory(simInventoryDto.getCategory());
		simInventory.setSpecialNumber(simInventoryDto.getSpecialNumber());
		simInventory.setImsi(simInventory.getImsi());
		simInventory.setPimsi(simInventoryDto.getPimsi());
		simInventory.setBatchId(simInventoryDto.getBatchId());
		simInventory.setBatchDate(simInventory.getBatchDate());
		simInventory.setVendorId(simInventoryDto.getVendorId());
		simInventory.setStatus(simInventoryDto.getStatus());
		simInventory.setProvStatus(simInventoryDto.getProvStatus());
		simInventory.setAllocationDate(simInventory.getAllocationDate());
		simInventory.setActivationDate(simInventory.getActivationDate());
//		simInventory.setVendorName(simInventoryDto.getVendorName());
//		simInventory.setVendorContact(simInventoryDto.getVendorContact());
//		simInventory.setVendorAddress(simInventoryDto.getVendorAddress());
		simInventory.setSimType(simInventoryDto.getSimType());
		simInventory.setBuyingPriceUsd(simInventoryDto.getBuyingPriceUsd());
		simInventory.setSellingPriceUsd(simInventoryDto.getSellingPriceUsd());
		simInventory.setVat(simInventoryDto.getVat());
		simInventory.setOtherTaxes(simInventoryDto.getOtherTaxes());
		simInventory.setMinCommision(simInventoryDto.getMinCommision());
		simInventory.setMaxCommision(simInventoryDto.getMaxCommision());
		simInventory.setAvgCommision(simInventoryDto.getAvgCommision());

		return mapper.map(simInventoryRepository.save(simInventory), SimInventoryDto.class);
	}

	@Override
	public List<SimInventoryDto> getAllSimInventory() {
		return simInventoryRepository.findAll().stream().map(simInventory -> mapper.map(simInventory, SimInventoryDto.class)).collect(Collectors.toList());
	}

	@Override
	public SimInventoryDto getByIdSimInventory(int simInventoryId) {
		SimInventory simInventory = simInventoryRepository.findById(simInventoryId).orElseThrow(() -> new ResourceNotFoundException("sim inventory not found with given id !!"));
		return mapper.map(simInventory, SimInventoryDto.class);
	}

	@Override
	public void deleteSimInventory(int simInventoryId) {
		SimInventory simInventory = simInventoryRepository.findById(simInventoryId).orElseThrow(() -> new ResourceNotFoundException("sim inventory not found with given id !!"));
		simInventoryRepository.delete(simInventory);
	}

	@Override
	public SimInventoryDto findByMsisdn(String msisdn) {
		SimInventory simInventory = simInventoryRepository.findByMsisdn(msisdn).orElseThrow(
				() -> new ResourceNotFoundException("sim inventory record not found with given msisdn !!"));
		return mapper.map(simInventory, SimInventoryDto.class);
	}

	@Override
	public void saveExcel(MultipartFile file, int vendorId) {
		
		 char c=(char) ('A' + new Random().nextInt(26));
		 String string = String.valueOf(System.currentTimeMillis());
		 String token = string + c;
		 //System.out.println(c);
		 //System.out.println(string); 
		//System.out.println(string + c);
		//System.out.println(token);
		
		Vendor vendor = vendorRepository.findById(vendorId).orElseThrow( () -> new ResourceNotFoundException("vendor not found with given id !!"));

		try {
			List<SimInventory> simInventories = ExcelHelper.convertExcelToListOfSimInventory(file.getInputStream());
			// simInventories.forEach(si -> System.out.println("@@@@@@@Excel Record
			// @@@@@@@@@@@@"+si));

			// int batchNumber = new Random().nextInt(99999999);

			List<SimInventory> simList = new ArrayList<>();
			
			simInventories.forEach(sim -> {
				SimInventory inventory = SimInventory.builder().msisdn(sim.getMsisdn()).category(sim.getCategory())
						.specialNumber(sim.getSpecialNumber()).imsi(sim.getImsi()).pimsi(sim.getPimsi())
						.batchId(sim.getBatchId()).vendorId(vendorId).status(false).provStatus(false)
						/*
						 * .vendorName(sim.getVendorName())
						 * .vendorAddress(sim.getVendorAddress()).vendorContact(sim.getVendorContact())
						 */
						.simType(sim.getSimType()).buyingPriceUsd(sim.getBuyingPriceUsd())
						.sellingPriceUsd(sim.getSellingPriceUsd()).vat(sim.getVat() + "%")
						.otherTaxes(sim.getOtherTaxes()).minCommision(sim.getMinCommision())
						.maxCommision(sim.getMaxCommision()).avgCommision(sim.getAvgCommision()).allocationDate(null)
						.activationDate(null).batchDate(LocalDate.now())
						.activationCode(new Random().nextInt(99999999) + "").activationToken(new Random().nextInt(999999999) + "").build();

				simList.add(inventory);
			});

			// simList.forEach(s ->System.out.println("########### SIM LIST ###############"+s));
			// System.out.println(simList);
			simInventoryRepository.saveAll(simList);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	

	@Override
	public List<PartnerSimResponse> PartnerAssignedSim(int partnerId) {
		//int count =0;
		List<PartnerSimResponse> response = new ArrayList<>();
		
		List<SimInventory> simList = simInventoryRepository.findByPartnerId(partnerId);
		
		for (SimInventory simInventory : simList) {
			
			PartnerSimResponse res = new PartnerSimResponse();
			
			res.setMsisdn(simInventory.getMsisdn());
			res.setImsi(simInventory.getImsi());
			res.setCategory(simInventory.getCategory());
			res.setSimType(simInventory.getSimType());
			res.setAmount(simInventory.getSellingPriceUsd());
			
			response.add(res);
		}
		
		return response;
	}

	@Override
	public Map<String, Map<String, Long>> allInventoryProductsCountOfPartner(int partnerId) {
	
		/*List<SimInventory> totalSim = simInventoryRepository.findByPartnerIdTotalSim(partnerId);
		//System.out.println("partner's total Sim count : " + totalSim.size());
		
		List<SimInventory> availableSim = totalSim.stream().filter(available -> available.getAllocationDate() == null).filter(activation -> activation.getActivationDate() == null).collect(Collectors.toList());
		//System.out.println("partner's available Sim count : " + availableSim.size());
		
		List<SimInventory> allocationSim = totalSim.stream().filter(allocate -> allocate.getAllocationDate() != null).collect(Collectors.toList());
		//System.out.println("partner's allocation Sim count : " + allocationSim.size());
		
		List<SimInventory> activationSim = totalSim.stream().filter(activate -> activate.getActivationDate() != null).collect(Collectors.toList());
		//System.out.println("partner's activation Sim count : " + activationSim.size());
		*/	
		
		List<SimInventory> simList = simInventoryRepository.findByPartnerIdTotalSim(partnerId);
		long totalSim = simList.stream().count();
		long availableSim = simList.stream().filter(available -> available.getAllocationDate() == null).filter(activation -> activation.getActivationDate() == null).count();
		long allocationSim = simList.stream().filter(allocate -> allocate.getAllocationDate() != null).count();	
		long activationSim = simList.stream().filter(activate -> activate.getActivationDate() != null).count();
		
		List<DeviceInventory> deviceList = deviceInventoryRepository.findByPartnerAllDevice(partnerId);
		long totalDevice = deviceList.stream().count();
		long availableDevice = deviceList.stream().filter(available ->available.getAllocationDate() == null).count();			
		long allocatedDevice = deviceList.stream().filter(d -> d.getAllocationDate() != null ).count();

		
		//router
		List<RouterInventory> routerList = routerInventoryRepository.findByPartnerId(partnerId);
		long totalRouter = routerList.stream().count();
		long availableRouter = routerList.stream().filter(router -> router.getAllocationDate() == null).count();
		long allocatedRouter = routerList.stream().filter(router -> router.getAllocationDate() != null).count();
		long activatedRouter = routerList.stream().filter(router -> router.getActivationDate() != null).count();
		
		Map<String, Long> sim = new HashMap<>();
		sim.put("totalSim", totalSim);
		sim.put("availableSim", availableSim);
		sim.put("allocatedSim", allocationSim);
		sim.put("activatedSim", activationSim);
		
		Map<String, Long> device = new HashMap<>();
		device.put("totalDevice", totalDevice);
		device.put("availableDevice", availableDevice);
		device.put("allocatedDevice", allocatedDevice);
		
		Map<String, Long> router = new HashMap<>();
		router.put("totalRouter", totalRouter);
		router.put("availableRouter", availableRouter);
		router.put("allocatedRouter", allocatedRouter);
		router.put("activatedRouter", activatedRouter);
		
		
		return Map.of("SIM",sim,"DEVICE",device,"Router",router);
	}

	@Override
	public Map<String, String> activationToken(String msisdn, String activationCode) {
		
		Map<String, String> response = new HashMap<>();
		
		SimInventory simInventory = simInventoryRepository.findByMsisdn(msisdn).orElseThrow( () -> new ResourceNotFoundException("sim not found with given msisdn"));
		//String activationCode2 = simInventory.getActivationCode();
		System.out.println(simInventory.getActivationCode()  + "+>" + activationCode);
		
		if (Integer.valueOf(simInventory.getActivationCode()).equals(Integer.valueOf(activationCode))) {
			
			response.put("activatinToken", simInventory.getActivationToken());
			
		}else {
			throw new ResourceNotFoundException("invalid activation code : " + activationCode);
		}
		
		
		return response;
	}

	@Override
	@Transactional
	public Map<String, String> simActivationAndDeactivation(String activationToken, String simService) {

		SimInventory simInventory = simInventoryRepository.findByActivationToken(activationToken).orElseThrow(() -> new ResourceNotFoundException("sim not found with given token"));
		Customer customer = customerRepository.findByMsisdn(simInventory.getMsisdn()).orElseThrow( () -> new ResourceNotFoundException("customer not found with given number !!"));
		
		/*if (simService.toLowerCase().equals("activate")) {
		
			simInventory.setActivationStatus("ACTIVATE");
			
			simInventoryRepository.save(simInventory);
			
			//pcf
			try {
				System.out.println("################## PCF/UDR Service ###########################");
				
				String URL=udrUrl+"change-profile";
				
				System.out.println("@@@@@@@@@@@@@"+URL);
				
				RestTemplate template = new RestTemplate();
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				
				Map<String, String> request = new HashMap<>();
				request.put("supi", simInventory.getImsi());
				request.put("subscriberProfile", "benefit");
				
				HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(request,headers);
				
				ResponseEntity<String> responseEntity = template.exchange(URL, HttpMethod.PUT,requestEntity,String.class);
				String responseBody = responseEntity.getBody();
				System.out.println("UDR response : " + responseBody);
				
			} catch (Exception e) {
				System.err.println(e.getMessage());
				throw new ResourceNotFoundException(e.getMessage());
				//e.printStackTrace();
			}
			
			return Map.of("message", "Sim Activate successfully !!");
			
		} else*/ if(simService.toLowerCase().equals("deactivate")){

			simInventory.setActivationStatus("DE-ACTIVATE");

			simInventoryRepository.save(simInventory);
			
			//pcf /udr
			try {
				System.out.println("################## PCF/UDR Service ###########################");
				
				String URL=udrUrl+"delete-profile";
				System.out.println("@@@@@@@@@@@@@"+URL);
				
				RestTemplate template = new RestTemplate();
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				
				Map<String, String> request = new HashMap<>();
				request.put("supi", simInventory.getImsi());
				request.put("subscriberProfile", "delete");
				
				HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(request,headers);
				
				ResponseEntity<String> responseEntity = template.exchange(URL, HttpMethod.DELETE,requestEntity,String.class);
				if (responseEntity.getStatusCode().is2xxSuccessful()) {
					
					String responseBody = responseEntity.getBody();
					System.out.println("UDR response : " + responseBody);
					
					//CRM
					//Customer customer = customerRepository.FindByMsisdn(simInventory.getMsisdn()).orElseThrow( () -> new ResourceNotFoundException("customer not found with given number !!"));
					customer.setServiceStatus(Customer.Status.DEACTIVATE);
					
					//ABMF
					try {
						System.out.println("################## ABMF Service ###########################");
						
						String URL_ABMF=abmfChangeStatusUrl;
						System.out.println("@@@@@@@@@@@@@"+URL_ABMF);
						
						RestTemplate restTemplate = new RestTemplate();
						
						HttpHeaders httpHeaders = new HttpHeaders();
						httpHeaders.setContentType(MediaType.APPLICATION_JSON);
						
						Map<String, String> httpRequest = new HashMap<>();
						httpRequest.put("customer_id", customer.getId()+"");
						httpRequest.put("user_status", "DEACTIVATE");
						
						HttpEntity<Map<String, String>> request_entity = new HttpEntity<>(httpRequest,httpHeaders);
						
						ResponseEntity<String> response_entity = restTemplate.exchange(URL_ABMF, HttpMethod.PUT, request_entity, String.class);
						
						if (response_entity.getStatusCode().is2xxSuccessful()) {

							String response_body = response_entity.getBody();
							System.out.println("ABMF response : " + response_body);
							
							//UDM
							
							try {
								
								System.out.println("************** UDM/HSS UN_PROVISIONING *******************************");
								
								String URL_UDM = UdmChangeStatusUrl + simInventory.getImsi();
								System.out.println("URL_UDM " + URL_UDM);
								
								RestTemplate rest_template = new RestTemplate();
								
								HttpHeaders http_headers = new HttpHeaders();
								http_headers.setContentType(MediaType.APPLICATION_JSON);
								
								HttpEntity<?> request_entity1 = new HttpEntity<>(http_headers);
								
								ResponseEntity<String> response_Entity = rest_template.exchange(URL_UDM, HttpMethod.PUT, request_entity1, String.class);
								
								String response_Body = response_Entity.getBody();
								System.out.println("UDM/HSS response : " + response_Body);
								
								
							} catch (Exception e) {
								e.printStackTrace();
							}
								
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return Map.of("message", "Sim De-Activate successfully !!");
		}else {
			throw new ResourceNotFoundException("pls share sim service activate/deactivate correctly !!");
		}

	}

	@Override
	@Transactional
	public ApiResponseMessage simActivationBySms(int shortCode,String text, String msisdn) {
		
		int short_code=121;	
		
		if (shortCode != short_code) {
			
			throw new ResourceNotFoundException("invalid short code. pls try with 121 !!");
		}
		
		if (!text.equalsIgnoreCase("ACT")) {
			throw new ResourceNotFoundException("invalid text. please try with ACT !!");
		}
		//sim
		SimInventory simInventory = simInventoryRepository.findByMsisdn(msisdn).orElseThrow( () -> new ResourceNotFoundException("sim not found with given msisdn !!"));	

		if (simInventory.getActivationStatus() == null) {
			
			simInventory.setActivationStatus("ACTIVATE");
			simInventoryRepository.save(simInventory);
			
		} else if (simInventory.getActivationStatus().equalsIgnoreCase("ACTIVATE")) {
			
			throw new ResourceNotFoundException("this msisdn is already Activate !!");
			
		}else if (simInventory.getActivationStatus().equalsIgnoreCase("DEACTIVATE")) {
			
			throw new ResourceNotFoundException("this msisdn is de-active. Please call customer care on 123 or visit our website www.neotel.nr");
		}
		//msisdn
		Customer customer = customerRepository.findByMsisdn(msisdn).orElseThrow( () -> new ResourceNotFoundException("customer not found with given msisdn !!"));
		//com.wpits.entities.Customer.Status serviceStatus = customer.getServiceStatus();
		
		if (customer.getServiceStatus() == null) {
			throw new ResourceNotFoundException("dear customer, your service status is not Active .  Please call customer care on 123 or visit our website www.neotel.nr");
		}
		if (customer.getServiceStatus().equals(Customer.Status.BLOCKED)) {
			
			throw new ResourceNotFoundException("dear customer, your service status is BLOCKED.  Please call customer care on 123 or visit our website www.neotel.nr");
			
		}
		if (customer.getServiceStatus().equals(Customer.Status.SUSPEND)) {
			
			throw new ResourceNotFoundException("dear customer, your service status is SUSPEND.  Please call customer care on 123 or visit our website www.neotel.nr");
			
		}
		//pcf
		try {
			System.out.println("################## PCF/UDR Service ###########################");
			
			String URL=udrUrl+"change-profile";
			
			System.out.println("@@@@@@@@@@@@@"+URL);
			
			RestTemplate template = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			Map<String, String> request = new HashMap<>();
			request.put("supi", simInventory.getImsi());
			request.put("subscriberProfile", "DEFAULT_DATA_IMS");
			
			HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(request,headers);
			
			ResponseEntity<String> responseEntity = template.exchange(URL, HttpMethod.PUT,requestEntity,String.class);
			
			String responseBody = responseEntity.getBody();
			System.out.println("UDR response : " + responseBody);
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new ResourceNotFoundException(e.getMessage());
			//e.printStackTrace();
		}
		return ApiResponseMessage.builder().message("Congratulations! Your SIM activation is successful, your MSISDN is : "+msisdn).success(true).status(HttpStatus.OK).build();
	}
	
	@Override
	public Map<String, String> getOtpByMsisdn(String msisdn) {
		SimInventory simInventory = simInventoryRepository.findByMsisdn(msisdn).orElseThrow( () -> new ResourceNotFoundException("sim not found with given msisdn !!"));
		
		//Customer customer = customerRepository.FindByMsisdn(msisdn).orElseThrow( () -> new ResourceNotFoundException("customer not found with given msisdn !!"));
		String activationCode = simInventory.getActivationCode();
		
		return Map.of("OTP", activationCode);
	}
	
	
	@Override
	@Transactional
	public ApiResponseMessage simActivationByWeb(String otp, String msisdn) {

		//sim
		SimInventory simInventory = simInventoryRepository.findByMsisdn(msisdn).orElseThrow(() -> new ResourceNotFoundException("sim not found with given msisdn !!"));
		
		if (!simInventory.getActivationCode().equals(otp)) {
			
			throw new ResourceNotFoundException("invalid otp !!");
		}
		if (simInventory.getActivationStatus() == null) {

			simInventory.setActivationStatus("ACTIVATE");
			simInventoryRepository.save(simInventory);

		} else if (simInventory.getActivationStatus().equalsIgnoreCase("ACTIVATE")) {

			throw new ResourceNotFoundException("this msisdn is already Activate !!");

		} else if (simInventory.getActivationStatus().equalsIgnoreCase("DEACTIVATE")) {

			throw new ResourceNotFoundException("this msisdn is de-activate.  Please call customer care on 123 or visit our website www.neotel.nr");
		}

		//msisdn
		Customer customer = customerRepository.findByMsisdn(msisdn).orElseThrow(() -> new ResourceNotFoundException("customer not found with given msisdn !!"));
		
		if (customer.getServiceStatus().equals(Customer.Status.BLOCKED)) {
			
			throw new ResourceNotFoundException("this msisdn is service BLOCKED.  Please call customer care on 123 or visit our website www.neotel.nr");
			
		}
		if (customer.getServiceStatus().equals(Customer.Status.SUSPEND)) {
			
			throw new ResourceNotFoundException("this msisdn is service SUSPEND.  Please call customer care on 123 or visit our website www.neotel.nr");
			
		}
		//pcf
		try {
			System.out.println("################## PCF/UDR Service ###########################");

			String URL = udrUrl + "change-profile";

			System.out.println("@@@@@@@@@@@@@" + URL);

			RestTemplate template = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, String> request = new HashMap<>();
			request.put("supi", simInventory.getImsi());
			request.put("subscriberProfile", "DEFAULT_DATA_IMS");

			HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(request, headers);

			ResponseEntity<String> responseEntity = template.exchange(URL, HttpMethod.PUT, requestEntity, String.class);

			String responseBody = responseEntity.getBody();
			System.out.println("UDR response : " + responseBody);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new ResourceNotFoundException(e.getMessage());
			//e.printStackTrace();
		}
		return ApiResponseMessage.builder().message("Congratulations! Your SIM activation is successful, your MSISDN is : "+msisdn).success(true).status(HttpStatus.OK).build();
	}



}
