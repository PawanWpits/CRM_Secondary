package com.wpits.services.serviceImpls;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.wpits.dtos.PartnerPaymentsDto;
import com.wpits.entities.CreditCard;
import com.wpits.entities.Currency;
import com.wpits.entities.DeviceInventory;
import com.wpits.entities.Partner;
import com.wpits.entities.PartnerInvoice;
import com.wpits.entities.PartnerOrder;
import com.wpits.entities.PartnerPayment;
import com.wpits.entities.SimInventory;
import com.wpits.exceptions.BadApiRequestException;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.CreditCardRepository;
import com.wpits.repositories.CurrencyRepository;
import com.wpits.repositories.DeviceInventoryRepository;
import com.wpits.repositories.PartnerInvoiceRepository;
import com.wpits.repositories.PartnerOrderRepository;
import com.wpits.repositories.PartnerPaymentsRepository;
import com.wpits.repositories.PartnerRepository;
import com.wpits.repositories.SimInventoryRepository;
import com.wpits.services.PartnerPaymentsService;

@Service
public class PartnerPaymentsServiceImpl implements PartnerPaymentsService{
	
	@Autowired
	private PartnerPaymentsRepository paymentRepository;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Autowired
	private CreditCardRepository creditCardRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;
	
	@Autowired
	private PartnerInvoiceRepository partnerInvoiceRepository;
	
	@Autowired
	private PartnerOrderRepository partnerOrderRepository;
	
	@Autowired
	private SimInventoryRepository simInventoryRepository;
	
	@Autowired
	private DeviceInventoryRepository deviceInventoryRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	private Logger logger = LoggerFactory.getLogger(PartnerPaymentsServiceImpl.class);

	@Override
	public PartnerPaymentsDto createPayment(PartnerPaymentsDto partnerPaymentsDto, int currencyId,int creditCard, String startingNumber, String endingNumber) {
		
		System.out.println("partner payment !!");
		
		/*if ( partnerPaymentsDto.getProduct() == null || partnerPaymentsDto.getProduct().trim().isEmpty()|| (!"Mobile device".equals(partnerPaymentsDto.getProduct()) && !"SIM".equals(partnerPaymentsDto.getProduct()) && !"CBM".equals(partnerPaymentsDto.getProduct()) )) {
			throw new BadApiRequestException("you are enter Product Wrong,pls check again !!");
		}*/	
		
		Partner partner = partnerRepository.findById(partnerPaymentsDto.getPartnerId()).orElseThrow( () -> new ResourceNotFoundException("partner not found with given id !!"));
		PartnerInvoice invoice = partnerInvoiceRepository.findByInvoiceNumber(partnerPaymentsDto.getInvoiceNumber()).orElseThrow( () -> new ResourceNotFoundException("invoice not found with given invoice number !!"));		
		Currency currency = currencyRepository.findById(currencyId).orElseThrow( () -> new ResourceNotFoundException("currency not found with given id !!"));
		
		PartnerPayment payment = mapper.map(partnerPaymentsDto, PartnerPayment.class);
		
		if (partnerPaymentsDto.getPartnerId() != invoice.getPartnerId()) {
			throw new BadApiRequestException("pls check the partnerId, it little bit different from the invoice's partnerId !!");
		}
		
		if (partnerPaymentsDto.getAmount() != invoice.getPayAmount()) {
			throw new BadApiRequestException("invalid amount !! pls check on invoice !!");
		}
			
		//PartnerPayment payment = new PartnerPayment();
		payment.setPartnerId(partner.getId());
		payment.setCurrency(currency.getId());
		payment.setProduct(invoice.getProduct());
		payment.setProductType(invoice.getProductType());
		payment.setTotalUnits(invoice.getPartnerOrders().getTotalUnits());
		payment.setAmount(invoice.getPayAmount());
		payment.setStatus(PartnerPayment.Status.PAID);
		payment.setPaymentDateTime(LocalDateTime.now());
		payment.setPartnerId(partner.getId());
		payment.setPaymentRefId("P-" + new Random().nextInt(999999999) + "" + (char) ('A' + new Random().nextInt(26)));
		payment.setPaymentMode(partnerPaymentsDto.getPaymentMode());
		if (creditCard >0) {
			CreditCard credtCard = creditCardRepository.findById(creditCard).orElseThrow( () -> new ResourceNotFoundException("credit card not found with given id !!"));	
			payment.setCreditCard(credtCard.getId());
			payment.setPaymentMode("CREDIT CARD");
		
		}

		//PartnerPayment payments = paymentRepository.save(payment);
		PartnerPaymentsDto payments = mapper.map(paymentRepository.save(payment), PartnerPaymentsDto.class);

		invoice.setPaymentStatus(true);
		partnerInvoiceRepository.save(invoice);
		
		PartnerOrder partnerOrders = invoice.getPartnerOrders();
		partnerOrders.setPaymentStatus(true);
		partnerOrderRepository.save(partnerOrders);
		
		//Mobile Device
		if (payments.getProduct().equalsIgnoreCase("Mobile device")) {
			
			System.out.println("partner payment share with Device !!");
			
			 List<DeviceInventory> devices = assignDeviceToPartner(invoice);
			 
			 if (devices.isEmpty()) {
				throw new ResourceNotFoundException("some thing went wrong ... device not Availabe !!");
			}
		}
		
		
		//SIM
		if (payments.getProduct().equalsIgnoreCase("SIM")) {
			System.out.println("partner payment share with SIM !!");
			
			if (startingNumber == null || startingNumber.trim().isEmpty() || endingNumber == null || endingNumber.trim().isEmpty()) {
			    throw new BadApiRequestException("startingNumber and endingNumber are required!");
			}
			List<SimInventory> assignSIMtoPartner = assignSIMtoPartner(payments, invoice, startingNumber, endingNumber);
			
			if (assignSIMtoPartner.isEmpty()) {
				throw new BadApiRequestException("series not found !!");
			}
		}
		
		//CBM
		if (payments.getProduct().equalsIgnoreCase("CBM")) {
			System.out.println("partner payment share with CBM !!");
			sendToCBM(payments, invoice, partner);
		}
		
		
		//return mapper.map(payments, PartnerPaymentsDto.class);
		return payments;
	}

	private List<DeviceInventory> assignDeviceToPartner(PartnerInvoice invoice) {
		
		System.out.println("assign device to partner !!");
		List<DeviceInventory> devices = new ArrayList<>();
		
		List<DeviceInventory> availableDevices = deviceInventoryRepository.findAvailableDevices(invoice.getProductType(), invoice.getTotalUnits());
		
		 if (availableDevices.size() < invoice.getTotalUnits()) {
			 
		        throw new ResourceNotFoundException("not enough available devices! Requested: "+ invoice.getTotalUnits() + ", Available: " + availableDevices.size());
		    }
		 
		 availableDevices.forEach(device -> device.setPartnerId(invoice.getPartnerId()));
		 
		 System.out.println("###########" +availableDevices);
		 
		 devices = deviceInventoryRepository.saveAll(availableDevices);
		 System.out.println("@@@@@@@@" + devices);
		 return devices;
		
	}

	private List<SimInventory> assignSIMtoPartner(PartnerPaymentsDto payments, PartnerInvoice invoice, String startingNumber,
			String endingNumber) {
		
		List<SimInventory> simList = new ArrayList<>();
		
		List<String> simRequest = new ArrayList<>();
		
		try {
			
			long start = Long.parseLong(startingNumber);
			long end = Long.parseLong(endingNumber);
			
			if ((end - start + 1) != invoice.getTotalUnits()) {
				throw new BadApiRequestException("this given series range totalUnits not match with given invoice totalUnits !!");
			}
			
			
			for (long i = start; i <= end; i++) {
				
				simRequest.add(String.valueOf(i));
			}
			
			// check available sims
		    List<SimInventory> availableSims = simInventoryRepository.findAvailableSimDetails(simRequest, invoice.getProductType());
		    System.out.println("availableSims :" + availableSims);
		    
		    // check unavailable Sims
		    List<String> availableSimNumbers = availableSims.stream().map(SimInventory::getMsisdn).collect(Collectors.toList()); 
		    List<String> unavailableSims = new ArrayList<>(simRequest);
		    unavailableSims.removeAll(availableSimNumbers);

		    if (!unavailableSims.isEmpty()) {
		        throw new ResourceNotFoundException("Unavailable SIMs : " + unavailableSims);
		    }
		    
	        // update partnerId available sim
	        availableSims.forEach(sim -> sim.setPartnerId(invoice.getPartnerId()));
	        simList = simInventoryRepository.saveAll(availableSims);

	        System.out.println("SIMs assigned successfully to Partner ID: " + invoice.getPartnerId());
			
		} catch (Exception e) {
			throw new BadApiRequestException(e.getMessage());
		}
		
		return simList;
	}

	private void sendToCBM(PartnerPaymentsDto payments, PartnerInvoice invoice,Partner partner) {

		try {
			
			System.out.println("################## Core Bal info share to CBM ###########################");

			final String CbmUrl = "http://172.17.1.24:9898/cbm/api/v1/partner/"+invoice.getPartnerId()+"/balance/buy";
			
			logger.info("This URL hit : {}", CbmUrl);
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String purchaseDate = payments.getPaymentDateTime().format(formatter);
			System.out.println("PurchaseDate : " + purchaseDate);

			Map<String, Object> request = new HashMap<>();
			request.put("msisdn", partner.getContact());
			request.put("creditBalance", invoice.getAmount());
			request.put("purchaseDate", purchaseDate);
			request.put("paymentId", payments.getPaymentRefId());
			request.put("amountPaid", invoice.getPayAmount());
			request.put("discountOffered", invoice.getOfferedDiscount());
			request.put("validityBind", false);
			request.put("validityPeriod", 0);
			request.put("childAgentEnabled", false);
			request.put("crmUserId", partner.getBaseUser().getId());
			

			logger.info("CRM request to CBM : {}", request);
			
			WebClient webClient = WebClient.create();
			String response = webClient.post().uri(CbmUrl).contentType(MediaType.APPLICATION_JSON).bodyValue(request)
					.retrieve().bodyToMono(String.class).block();

			logger.info("CBM response: {}", response);

		} catch (Exception e) {
			logger.error("Error from CBM: {}", e.getMessage());
		}

	}

	@Override
	public List<PartnerPaymentsDto> getPayments() {
		
		return paymentRepository.findAll().stream().map( payment -> mapper.map(payment, PartnerPaymentsDto.class)).collect(Collectors.toList());
	}

}
