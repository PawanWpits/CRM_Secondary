package com.wpits.services.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.entities.Customer;
import com.wpits.entities.CustomerInfo;
import com.wpits.entities.Payment;
import com.wpits.entities.SimInventory;
import com.wpits.repositories.CustomerInfoRepository;
import com.wpits.repositories.CustomerRepository;
import com.wpits.repositories.SimInventoryRepository;
import com.wpits.repositories.PaymentRepository;
import com.wpits.services.CustomerInfoService;

@Service
public class CustomerInfoServiceImpl implements CustomerInfoService {

	@Autowired
	private CustomerInfoRepository customerInfoRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private SimInventoryRepository simInventoryRepository;
	@Autowired
	private PaymentRepository paymentRepository;

	@Override
	public void saveCustoomerInfo() {
		// List<Customer> customers = customerRepository.findAll();
		// customers.stream().filter( customer -> customer.getId() !=customerInfoRepository.findById(customer.getId())).forEach(customer->System.out.println(customer));
		List<Customer> customers = customerRepository.findAll();
		

		for (Customer customer : customers) {
			CustomerInfo custInfo = new CustomerInfo();
			try {
				int id = customer.getId();

				//custInfo.setCustomerId(id);
				
				try {
					CustomerInfo customerInfo = customerInfoRepository.findByCustomerId(id).get();
					if (customerInfo.getCustomerId() == id) {
						continue;
					} else {
						custInfo.setCustomerId(id);
					}
				} catch (Exception e) {
					custInfo.setCustomerId(id);
					System.out.println(e.getMessage());
					System.out.println("!!!!!!!!!!!!");					
				}
				

				/*
				 * try { SimInventory simInventory =
				 * simInventoryRepository.findById(customer.getSimInventory().getId()).get();
				 * 
				 * if (simInventory != null) {
				 * custInfo.setMsisdnInventoryId(simInventory.getId()); } else {
				 * custInfo.setMsisdnInventoryId(0); } } catch (Exception e) {
				 * custInfo.setMsisdnInventoryId(0); System.out.println(e.getMessage());
				 * System.out.println("@@@@@@@@"); }
				 */

				try {
					Payment payment = paymentRepository.findByCustomerId(customer.getId()).get();

					if (payment != null) {
						custInfo.setPaymentId(payment.getId());
					} else {
						custInfo.setPaymentId(0);
					}
				} catch (Exception e) {
					custInfo.setPaymentId(0);
					System.out.println(e.getMessage());
					System.out.println("########");
				}
				

				CustomerInfo info = customerInfoRepository.save(custInfo);
				System.out.println(info);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
