package com.wpits.services.serviceImpls;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.PartnerOrderRequest;
import com.wpits.entities.DeviceInventory;
import com.wpits.entities.Partner;
import com.wpits.entities.PartnerInvoice;
import com.wpits.entities.PartnerOrder;
import com.wpits.entities.RouterInventory;
import com.wpits.entities.SimInventory;
import com.wpits.exceptions.BadApiRequestException;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.DeviceInventoryRepository;
import com.wpits.repositories.PartnerInvoiceRepository;
import com.wpits.repositories.PartnerOrderRepository;
import com.wpits.repositories.PartnerRepository;
import com.wpits.repositories.RouterInventoryRepository;
import com.wpits.repositories.SimInventoryRepository;
import com.wpits.services.PartnerOrderService;

@Service
public class PartnerOrderServiceImpl implements PartnerOrderService {

	@Autowired
	private PartnerOrderRepository partnerOrderRepository;

	@Autowired
	private SimInventoryRepository simInventoryRepository;
	
	@Autowired
	private DeviceInventoryRepository deviceInventoryRepository;
	
	@Autowired
	private RouterInventoryRepository routerInventoryRepository;

	@Autowired
	private PartnerInvoiceRepository partnerInvoiceRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;

	private static final String TRANSACTION = "transactionID";
	private static final String AMOUNT = "payAmount";
	
	public Map<String, Object>  createPartnerOrder(PartnerOrderRequest partnerOrderRequest) {
		
		//ApiResponseMessage response = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if (partnerOrderRequest.getProduct() == null && partnerOrderRequest.getProduct().trim().isEmpty()) {
			throw new BadApiRequestException("product can not be null !!");
		}
		
		if (partnerOrderRequest.getProductType() == null || partnerOrderRequest.getProductType().trim().isEmpty()) {
			throw new BadApiRequestException("product type can not be null !!");
		}
		
		if (partnerOrderRequest.getTotalUnits() == null && partnerOrderRequest.getTotalUnits()<=0) {
			throw new BadApiRequestException("totalUnits must be greater than 0 !!");
		}
		
		Partner partner = partnerRepository.findById(partnerOrderRequest.getPartnerId()).orElseThrow( () -> new ResourceNotFoundException("partner not found with given id !!"));
		
		
		if (partnerOrderRequest.getProduct().equalsIgnoreCase("CBM")) {
			
			System.out.println("################# Product CBM ################");
			response = createCbmOrder(partnerOrderRequest, partner);	
		}
		
		if (partnerOrderRequest.getProduct().equalsIgnoreCase("SIM")) {
			
			System.out.println("################# Product SIM ################");
			response = createSimOrder(partnerOrderRequest, partner);
		}
		
		if (partnerOrderRequest.getProduct().equalsIgnoreCase("Mobile device")) {
			
			System.out.println("################# Product Mobile device ################");
			response = createDeviceOrder(partnerOrderRequest);
		}

		if (partnerOrderRequest.getProduct().equalsIgnoreCase("Broadband")) {

			System.out.println("################# Product Broadband ################");
			response = createBroadbandOrder(partnerOrderRequest);
		}

		return response;

	}

	private Map<String, Object> createBroadbandOrder(PartnerOrderRequest partnerOrderRequest) {

		System.out.println("################# Create MOBILE DEVICE ################");
		
		//String model = partnerOrderRequest.getProductType();
		
		List<RouterInventory> availableRouters = routerInventoryRepository.findAvailableDevices(partnerOrderRequest.getProductType(), partnerOrderRequest.getTotalUnits());
		
		 if (availableRouters.size() < partnerOrderRequest.getTotalUnits()) {
			 
		        throw new ResourceNotFoundException("not enough available devices! Requested: "+ partnerOrderRequest.getTotalUnits() + ", Available: " + availableRouters.size());
		    }
		 
		 //double amount = availableRouters.stream().mapToDouble(DeviceInventory::getBuyingPriceUsd).sum();
		 double amount = 0.0;
		 //router order
		 PartnerOrder partnerOrder = createOrder(partnerOrderRequest, amount);	 
		 //invoice 
		 PartnerInvoice partnerInvoice = generateInvoice(partnerOrderRequest, partnerOrder);
		 
		 //partner set
		 availableRouters.forEach(router -> router.setPartnerId(partnerOrderRequest.getPartnerId()));
		 List<RouterInventory> routers = routerInventoryRepository.saveAll(availableRouters);
		 if (routers.isEmpty()) {
			throw new BadApiRequestException("some thing went wrong !!");
		}
		 
		List<String> macAddress = routers.stream().map(RouterInventory::getMacAddress).collect(Collectors.toList());
		 
		 return Map.of(TRANSACTION,partnerInvoice.getInvoiceNumber(),AMOUNT,partnerInvoice.getPayAmount(),"message","payment not required","routerCount",partnerOrderRequest.getTotalUnits(),"router's macAddress",macAddress);		 

	}

	private Map<String, Object> createDeviceOrder(PartnerOrderRequest partnerOrderRequest) {
		
		System.out.println("################# Create MOBILE DEVICE ################");
		
		//String model = partnerOrderRequest.getProductType();
		
		List<DeviceInventory> availableDevices = deviceInventoryRepository.findAvailableDevices(partnerOrderRequest.getProductType(), partnerOrderRequest.getTotalUnits());
		
		 if (availableDevices.size() < partnerOrderRequest.getTotalUnits()) {
			 
		        throw new ResourceNotFoundException("not enough available devices! Requested: "+ partnerOrderRequest.getTotalUnits() + ", Available: " + availableDevices.size());
		    }
		 
		 double amount = availableDevices.stream().mapToDouble(DeviceInventory::getBuyingPriceUsd).sum();
		 
		 //device order
		 PartnerOrder partnerOrder = createOrder(partnerOrderRequest, amount);	 
		 //invoice 
		 PartnerInvoice partnerInvoice = generateInvoice(partnerOrderRequest, partnerOrder);
		 
		 return Map.of(TRANSACTION,partnerInvoice.getInvoiceNumber(),AMOUNT,partnerInvoice.getPayAmount());		 
	}

	private Map<String, Object>  createCbmOrder(PartnerOrderRequest partnerOrderRequest, Partner partner) {
		
		double amount =partnerOrderRequest.getTotalUnits();
		
		//create orderrr
		PartnerOrder partnerOrder = createOrder(partnerOrderRequest, amount);
		//generatee orderr
		PartnerInvoice partnerInvoice = generateInvoice(partnerOrderRequest, partnerOrder);
		
		return Map.of(TRANSACTION,partnerInvoice.getInvoiceNumber(),AMOUNT,partnerInvoice.getPayAmount());
	}

	private Map<String, Object> createSimOrder(PartnerOrderRequest partnerOrderRequest, Partner partner) {
		
		//Partner partner = partnerRepository.findById(partnerOrderRequest.getPartnerId()).orElseThrow( () -> new ResourceNotFoundException("partner not found with given id !!"));
		
		if (partnerOrderRequest.getProductType() == null || partnerOrderRequest.getProductType().trim().isEmpty()) {
			throw new BadApiRequestException("productType can not be null !!");
		}
		
		if (partnerOrderRequest.getStartingNumber() == null || partnerOrderRequest.getStartingNumber().trim().isEmpty() || partnerOrderRequest.getEndingNumber() == null || partnerOrderRequest.getEndingNumber().trim().isEmpty()) {
			    throw new BadApiRequestException("startingNumber and endingNumber are required!");
			}

		// check total units with given MSISDN series
		long start = Long.parseLong(partnerOrderRequest.getStartingNumber());
		long end = Long.parseLong(partnerOrderRequest.getEndingNumber());

		if ((end - start + 1) != partnerOrderRequest.getTotalUnits()) {
			throw new BadApiRequestException("totalUnits not match with given series range !!");
		}

		// list of request msisdns
		List<String> simRequest = new ArrayList<>();
		System.out.println("requestedSims 1" + simRequest);

		System.out.println("start : " +start +" :: "+ "end : " +end);
		for (long i = start; i <= end; i++) {

			simRequest.add(String.valueOf(i));
		}

		System.out.println("requestedSims 2" + simRequest);
		// Check available Sims
	    List<SimInventory> availableSims = simInventoryRepository.findAvailableSimDetails(simRequest, partnerOrderRequest.getProductType());
	    System.out.println("availableSims :" + availableSims);

	    // Check unavailable Sims
	    List<String> availableSimNumbers = availableSims.stream().map(SimInventory::getMsisdn).collect(Collectors.toList()); 
	    List<String> unavailableSims = new ArrayList<>(simRequest);
	    unavailableSims.removeAll(availableSimNumbers);

	    if (!unavailableSims.isEmpty()) {
	        throw new ResourceNotFoundException("Unavailable SIMs : " + unavailableSims);
	    }

	    // calculate total amount sims
	    double amount = availableSims.stream().mapToDouble(SimInventory::getBuyingPriceUsd).sum();
		System.out.println("amount :" + amount);
		
		// order create
		PartnerOrder partnerOrder = createOrder(partnerOrderRequest,amount);
		
		// Invoice generatee
		PartnerInvoice partnerInvoice = generateInvoice(partnerOrderRequest,partnerOrder);

		return Map.of(TRANSACTION,partnerInvoice.getInvoiceNumber(),AMOUNT,partnerInvoice.getPayAmount());
	}


	private PartnerInvoice generateInvoice(PartnerOrderRequest partnerOrderRequest, PartnerOrder partnerOrder) {

		PartnerInvoice partnerInvoice = null;

		try {

			PartnerInvoice invoice = new PartnerInvoice();
			invoice.setPartnerOrders(partnerOrder);
			invoice.setAmount(partnerOrder.getTotalAmount());
			invoice.setOfferedDiscount(partnerOrderRequest.getOfferedDiscount());
			invoice.setPayAmount(partnerOrder.getTotalAmount());

			if (invoice.getOfferedDiscount() > 0) {
				final double discount = partnerOrder.getTotalAmount() * partnerOrderRequest.getOfferedDiscount() / 100;
				invoice.setPayAmount(roundToTwoDecimalPlaces(partnerOrder.getTotalAmount() - discount));
			}

			invoice.setInvoiceDate(LocalDateTime.now());
			invoice.setDueDate(LocalDateTime.now());
			invoice.setInvoiceNumber("PIV-" + new Random().nextInt(999999999) + "" + (char) ('A' + new Random().nextInt(26)));
			invoice.setProduct(partnerOrder.getProduct());
			invoice.setProductType(partnerOrder.getProductType());
			invoice.setPartnerId(partnerOrder.getPartnerId());
			invoice.setTotalUnits(partnerOrder.getTotalUnits());
			partnerInvoice = partnerInvoiceRepository.save(invoice);

			System.out.println("order created successfully with Invoice Number: " + partnerInvoice.getInvoiceNumber());
		} catch (Exception e) {
			throw new BadApiRequestException(e.getMessage());
		}
		return partnerInvoice;
	}

	private PartnerOrder createOrder(PartnerOrderRequest partnerOrderRequest, double amount) {

		PartnerOrder partnerOrder = null;
		
		try {

			PartnerOrder order = new PartnerOrder();
			order.setPartnerId(partnerOrderRequest.getPartnerId());
			order.setProduct(partnerOrderRequest.getProduct());
			order.setProductType(partnerOrderRequest.getProductType());
			order.setTotalUnits(partnerOrderRequest.getTotalUnits());
			order.setTotalAmount(roundToTwoDecimalPlaces(amount));
			order.setOrderDate(LocalDateTime.now());
			order.setOrderNumber("POD-" + new Random().nextInt(999999999) + "" + (char) ('A' + new Random().nextInt(26)));
			partnerOrder = partnerOrderRepository.save(order);

		} catch (Exception e) {
			throw new BadApiRequestException(e.getMessage());
		}
		return partnerOrder;
	}

	private double roundToTwoDecimalPlaces(double value) {

		return Math.round(value * 100.0) / 100.0;
	}

}
