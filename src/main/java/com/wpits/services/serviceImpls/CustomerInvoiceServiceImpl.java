package com.wpits.services.serviceImpls;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpits.dtos.CustomerInvoiceDto;
import com.wpits.dtos.InvoiceItemDTO;
import com.wpits.entities.Customer;
import com.wpits.entities.CustomerInvoice;
import com.wpits.entities.Order;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.CustomerInvoiceRepository;
import com.wpits.repositories.CustomerRepository;
import com.wpits.repositories.OrderRepository;
import com.wpits.services.CustomerInvoiceService;

@Service
public class CustomerInvoiceServiceImpl implements CustomerInvoiceService {

	@Autowired
	private CustomerInvoiceRepository customerInvoiceRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CustomerInvoiceDto findByOrderNumber(String orderNumber) {
		
		CustomerInvoice invoice = customerInvoiceRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new ResourceNotFoundException("invoice not found with given order number !!"));

		/*Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new ResourceNotFoundException("order not found with given order number !!"));
		String token = order.getToken();
		
		if (token == null) {
			throw new ResourceNotFoundException("token required !!");
		}
		
		List<Customer> customers = customerRepository.findByEkycToken(token);
		
		if (customers.isEmpty()) {
			throw new ResourceNotFoundException("customer not found with given token !!");
		}
		
		Customer customer = customers.get(0);
		
		order.setName(customer.getFirstName()== null ? "" : customer.getFirstName() +" "+ customer.getMaidenName()== null ? "" : customer.getMaidenName()+" "+customer.getLastName()== null ? "" : customer.getLastName());
		orderRepository.save(order);
		*/
		
		CustomerInvoiceDto invoiceDTO = new CustomerInvoiceDto();
		
		invoiceDTO.setInvoiceId(invoice.getId());
		invoiceDTO.setOrderNumber(invoice.getOrderNumber());
		invoiceDTO.setCustomerName(invoice.getCustomerName());
		//invoiceDTO.setCustomerName(customer.getFirstName()== null ? "" : customer.getFirstName() +" "+ customer.getMaidenName()== null ? "" : customer.getMaidenName()+" "+customer.getLastName()== null ? "" : customer.getLastName());
		invoiceDTO.setOrderDate(invoice.getDate());
		invoiceDTO.setTotalAmount(invoice.getTotalAmount());
		invoiceDTO.setInvoiceReferenceNo(invoice.getInvoiceReferenceNo());
		invoiceDTO.setInvoiceDateTime(invoice.getInvoiceDateTime());

		try {
			
			//List<InvoiceItemDTO> items = invoice.getDetails();
			List<InvoiceItemDTO> items = objectMapper.readValue(invoice.getDetails(),new TypeReference<List<InvoiceItemDTO>>() {});
			
			invoiceDTO.setItems(items);
			
		} catch (Exception e) {
			
			throw new ResourceNotFoundException("Failed to parse invoice details"+ e.getMessage());
		}

		return invoiceDTO;
	}

//	@Override
//	public CustomerInvoiceDto createInvoice(int orderId) {
//		return null;
//		Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order not found with given id !!"));
//		
////		CustomerInvoice customerInvoice = new CustomerInvoice();
////		//customerInvoice.setOrderId(order.getId());
////		customerInvoice.setAmount(null);
////		customerInvoice.setInvoiceDate(LocalDateTime.now());
//		
//		return mapper.map(customerInvoiceRepository.save(customerInvoice), CustomerInvoiceDto.class);
//	}

	/*
	 * @Override public CustomerInvoiceDto findByOrderId(int orderId) {
	 * CustomerInvoice customerInvoice =
	 * customerInvoiceRepository.findByOrderId(orderId).orElseThrow( () -> new
	 * ResourceNotFoundException("customer invoice not found with given order id !!"
	 * )); return mapper.map(customerInvoice, CustomerInvoiceDto.class); }
	 */

}
