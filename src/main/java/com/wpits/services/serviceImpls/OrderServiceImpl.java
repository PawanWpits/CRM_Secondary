package com.wpits.services.serviceImpls;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpits.dtos.InvoiceItemDTO;
import com.wpits.dtos.OrderItemDTO;
import com.wpits.dtos.OrderResponseDTO;
import com.wpits.entities.Cart;
import com.wpits.entities.Customer;
import com.wpits.entities.CustomerDeviceInfo;
import com.wpits.entities.CustomerInvoice;
import com.wpits.entities.DeviceInventory;
import com.wpits.entities.Order;
import com.wpits.entities.OrderItem;
import com.wpits.entities.RouterInventory;
import com.wpits.entities.SimInventory;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.CartRepository;
import com.wpits.repositories.CustomerDeviceInfoRepository;
import com.wpits.repositories.CustomerInvoiceRepository;
import com.wpits.repositories.CustomerRepository;
import com.wpits.repositories.DeviceInventoryRepository;
import com.wpits.repositories.OrderRepository;
import com.wpits.repositories.RouterInventoryRepository;
import com.wpits.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerInvoiceRepository customerInvoiceRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CustomerDeviceInfoRepository customerDeviceInfoRepository;

	@Autowired
	private DeviceInventoryRepository deviceInventoryRepository;

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private RouterInventoryRepository routerInventoryRepository;

	@Override
	@Transactional
	public OrderResponseDTO createOrder(String token) {

		double totalAmount = 0;
		int deviceId = 0;
		int routerSerialNo = 0;

		List<OrderItemDTO> orderItemsResponse = new ArrayList<>();

		List<Customer> customers = customerRepository.findByEkycToken(token);
		System.out.println("Total customer product paid and unpaid: " + customers.size());
		if (customers.isEmpty()) {
			throw new ResourceNotFoundException("Customer not found with given token !!");
		}

		/*		List<Customer> unpaidCustomerProduct = customers.stream().filter(customer -> customer.getPayment() == null).collect(Collectors.toList());
				System.out.println("Unpaid product customer: " + unpaidCustomerProduct.size());*/

		Customer cust = customers.get(0);

		//Check unpaid order
		// 
        Order unpaidOrder = orderRepository.findByTokenAndPaymentStatusIsFalse(token);
        
        Order order;
        
        if (unpaidOrder != null) {
        	
        	order=unpaidOrder;
        	//clear order_item 
        	order.getOrderItems().clear(); 
        	
		} else {

			order = new Order();

			order.setToken(token);
			order.setDate(LocalDate.now());

			String firstName = cust.getFirstName() == null ? "" : cust.getFirstName();
			String maidenName = cust.getMaidenName() == null ? "" : cust.getMaidenName();
			String lastName = cust.getLastName() == null ? "" : cust.getLastName();
			
			order.setName(firstName + " " + maidenName + " " + lastName);

			if (cust.getPartner() != null) {
				
				order.setPartnerId(cust.getPartner().getId());
			}

			//order.setOrderNumber(new Random().nextInt(999999999)+"");
			order.setOrderNumber("OD" + new Random().nextInt(999999999)+"" + (char) ('A' + new Random().nextInt(26)));
		}
		
		List<Customer> unpaidCustomerProduct = customers.stream().filter(customer -> customer.getPayment() == null).collect(Collectors.toList());
		System.out.println("Unpaid product customer: " + unpaidCustomerProduct.size());
        
		for (Customer customer : unpaidCustomerProduct) {

			System.out.println("@@@@@ cust Id " + customer.getId());
			
			//SIM
			SimInventory simInventory = customer.getSimInventory();

			if (simInventory != null) {

				String simType = customer.getCustomerType();
				double price = simInventory.getSellingPriceUsd();
				String productType = simInventory.getSimType() != null ? simInventory.getSimType() : "Unknown"; //unknown default value rkh li
				String number = simInventory.getMsisdn();
				int productSerialNo = simInventory.getId();

				totalAmount += price;

				OrderItem simItem = new OrderItem();
				
				simItem.setName(simType);
				simItem.setQuantity(1);
				simItem.setPrice(roundToTwoDecimalPlaces(price));
				simItem.setProductType(productType);
				simItem.setNumber(number);
				simItem.setProductSerialNo(productSerialNo);
				simItem.setOrder(order);

				order.getOrderItems().add(simItem);
				orderItemsResponse.add(convertToDTO(simItem));
			}
			
			//DEVICE 
			DeviceInventory deviceInventory = customer.getDeviceInventory();
			
			if (deviceInventory != null) {

				String deviceType = deviceInventory.getDeviceModel();
				double price = deviceInventory.getSellingPriceUsd();
				String productType = deviceInventory.getDeviceType() != null ? deviceInventory.getDeviceType(): "Unknown"; //default value unknown rkh li 
				String number = deviceInventory.getImi();
				int productSerialNo = deviceInventory.getId();

				totalAmount += price;

				OrderItem deviceItem = new OrderItem();

				deviceItem.setName(deviceType);
				deviceItem.setQuantity(1);
				deviceItem.setPrice(roundToTwoDecimalPlaces(price));
				deviceItem.setProductType(productType);
				deviceItem.setNumber(number);
				deviceItem.setProductSerialNo(productSerialNo);
				deviceItem.setOrder(order);

				order.getOrderItems().add(deviceItem);
				orderItemsResponse.add(convertToDTO(deviceItem));
			}
			
			//Router
			RouterInventory routerInventory = customer.getRouterInventory();
			
			if (routerInventory != null) {
				

				String brandName = routerInventory.getBrand();
				String routerType = routerInventory.getType();
				int serialNo = routerInventory.getSerialNumber();
				String routerId = routerInventory.getId();
				
				totalAmount += 0.0; 
				
				OrderItem routerItem = new OrderItem();
				
				routerItem.setName(brandName);
				routerItem.setQuantity(1);
				routerItem.setPrice(roundToTwoDecimalPlaces(0.0));
				routerItem.setProductType(routerType);
				routerItem.setProductSerialNo(serialNo);
				routerItem.setNumber(routerId);
				routerItem.setOrder(order);
				
				order.getOrderItems().add(routerItem);
				orderItemsResponse.add(convertToDTO(routerItem));
				
			}
		
		}

		List<Cart> cartItems = cartRepository.findByToken(token);
		System.out.println("Total devices count: " + cartItems.size());

		if (!cartItems.isEmpty() && cartItems.size() > 0) {

			//Cart cart_ = cartItems.get(0);
			Cart cart_ = cartItems.get(cartItems.size() - 1);

			if (cart_.getPartnerId() > 0) {

				order.setPartnerId(cart_.getPartnerId());
			}

			for (Cart cart : cartItems) {

				deviceId = cart.getDeviceId();
				System.out.println("#### deviceId: " + deviceId);

				DeviceInventory deviceInventory = deviceInventoryRepository.findById(deviceId).orElse(null);

				if (deviceInventory != null) {

					String deviceType = deviceInventory.getDeviceModel();
					double price = deviceInventory.getSellingPriceUsd();
					String productType = deviceInventory.getDeviceType() != null ? deviceInventory.getDeviceType(): "Unknown"; //default value unknown rkh li 
					String number = deviceInventory.getImi();
					int productSerialNo = deviceInventory.getId();

					totalAmount += price;

					OrderItem deviceItem = new OrderItem();

					deviceItem.setName(deviceType);
					deviceItem.setQuantity(1);
					deviceItem.setPrice(roundToTwoDecimalPlaces(price));
					deviceItem.setProductType(productType);
					deviceItem.setNumber(number);
					deviceItem.setProductSerialNo(productSerialNo);
					deviceItem.setOrder(order);

					order.getOrderItems().add(deviceItem);
					orderItemsResponse.add(convertToDTO(deviceItem));
				}
				
				//router
				routerSerialNo = cart.getRouterSerialNo();
				System.out.println("## Router Serial No." + routerSerialNo);
				
				RouterInventory routerInventory = routerInventoryRepository.findBySerialNumber(routerSerialNo).orElse(null);
				
				if (routerInventory != null) {
					

					String brandName = routerInventory.getBrand();
					String routerType = routerInventory.getType();
					int serialNo = routerInventory.getSerialNumber();
					String routerId = routerInventory.getId();
					
					totalAmount += 0.0; 
					
					OrderItem routerItem = new OrderItem();
					
					routerItem.setName(brandName);
					routerItem.setQuantity(1);
					routerItem.setPrice(roundToTwoDecimalPlaces(0.0));
					routerItem.setProductType(routerType);
					routerItem.setProductSerialNo(serialNo);
					routerItem.setNumber(routerId);
					routerItem.setOrder(order);
					
					order.getOrderItems().add(routerItem);
					orderItemsResponse.add(convertToDTO(routerItem));
					
				}
				
			}
		}

		double total_amount = roundToTwoDecimalPlaces(totalAmount);
		System.out.println("Total amount: " + total_amount);

		order.setTotalAmount(total_amount);

		orderRepository.save(order);

		OrderResponseDTO response = new OrderResponseDTO();

		response.setId(order.getId());
		response.setName(order.getName());
		response.setToken(token);
		response.setDate(order.getDate());
		response.setPartnerId(order.getPartnerId());
		response.setOrderNumber(order.getOrderNumber());
		response.setOrderDetails(orderItemsResponse);
		response.setTotalAmount(totalAmount);

		generateInvoice(response);

		return response;
	}

	private OrderItemDTO convertToDTO(OrderItem item) {

		OrderItemDTO dto = new OrderItemDTO();

		dto.setName(item.getName());
		dto.setQuantity(item.getQuantity());
		dto.setPrice(item.getPrice());
		dto.setProductType(item.getProductType());
		dto.setNumber(item.getNumber());
		dto.setProductSerialNo(item.getProductSerialNo());

		return dto;
	}

	private void generateInvoice(OrderResponseDTO response) {

		int simTaxRate = 2; // 2% tax 
		int deviceTaxRate = 4; // 4% tax

		String customerName = response.getName();
		String orderNumber = response.getOrderNumber();
		LocalDate orderDate = response.getDate();

		List<InvoiceItemDTO> invoiceItems = new ArrayList<>();

		double totalTax = 0;

		for (OrderItemDTO item : response.getOrderDetails()) {

			InvoiceItemDTO invoiceItem = new InvoiceItemDTO();

			invoiceItem.setName(item.getName());
			invoiceItem.setProductType(item.getProductType());
			invoiceItem.setQuantity(item.getQuantity());
			invoiceItem.setPrice(roundToTwoDecimalPlaces(item.getPrice()));
			invoiceItem.setNumber(item.getNumber());
			invoiceItem.setProductSerialNo(item.getProductSerialNo());

			String productType = item.getProductType() != null ? item.getProductType().toLowerCase() : "";

			double taxRate = productType.contains("sim") ? simTaxRate : deviceTaxRate;
			double taxAmount = roundToTwoDecimalPlaces(item.getPrice() * taxRate / 100);
			double priceWithTax = roundToTwoDecimalPlaces(item.getPrice() + taxAmount);

			invoiceItem.setTaxRate(taxRate);
			invoiceItem.setTaxAmount(taxAmount);
			invoiceItem.setPriceWithTax(priceWithTax);

			invoiceItems.add(invoiceItem);
			totalTax += taxAmount;
		}

		double totalAmountWithTax = roundToTwoDecimalPlaces(response.getTotalAmount() + totalTax);

		saveInvoice(orderNumber, customerName, orderDate, invoiceItems, totalAmountWithTax);
	}

	private void saveInvoice(String orderNumber, String customerName, LocalDate orderDate, List<InvoiceItemDTO> invoiceItems, double totalAmountWithTax) {
		
		try {
			
			CustomerInvoice orderInvoice = customerInvoiceRepository.findByOrderNumber(orderNumber).orElse(null);
			
			if (orderInvoice != null) {
				
				orderInvoice.setOrderNumber(orderNumber);
				orderInvoice.setCustomerName(customerName);
				orderInvoice.setDate(orderDate);
				orderInvoice.setInvoiceReferenceNo(UUID.randomUUID().toString());
				orderInvoice.setInvoiceDateTime(LocalDateTime.now());
				//orderInvoice.setDetails(invoiceItems);
				orderInvoice.setDetails(objectMapper.writeValueAsString(invoiceItems));
				orderInvoice.setTotalAmount(totalAmountWithTax);
				
				customerInvoiceRepository.save(orderInvoice);
				
			} else {

				CustomerInvoice order_invoice = new CustomerInvoice();

				order_invoice.setOrderNumber(orderNumber);
				order_invoice.setCustomerName(customerName);
				order_invoice.setDate(orderDate);
				order_invoice.setInvoiceReferenceNo(UUID.randomUUID().toString());
				order_invoice.setInvoiceDateTime(LocalDateTime.now());
				//order_invoice.setDetails(invoiceItems);
				order_invoice.setDetails(objectMapper.writeValueAsString(invoiceItems));
				order_invoice.setTotalAmount(totalAmountWithTax);

				customerInvoiceRepository.save(order_invoice);
			}


		} catch (Exception e) {

			System.err.println("@@ customer invoice" + e);
			throw new ResourceNotFoundException("Failed to save invoice  " + e.getMessage());
		}
	}

	private double roundToTwoDecimalPlaces(double value) {
		
		return Math.round(value * 100.0) / 100.0;
	}
}