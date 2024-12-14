package com.wpits.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wpits.services.CartService;
import com.wpits.services.CustomerService;
import com.wpits.services.InvoiceService;
import com.wpits.services.PaymentService;

@Component
public class InvoiceCronJobSchedular {

	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private PaymentService paymentService;
	
	/*	
		@Scheduled(cron = "0 0 0 15 * ?", zone = "GMT+12")    //@Async bhi use kr skte hai jb object jayda ho to new tread chelga is method ke liye bina kisi ko distrub kiye
		public void genrateInvoice() {
			invoiceService.genrateInvoice();
		}
		                                                      //@Scheduled(cron = "0 * * * * ?", zone = "GMT+12")//every minute
		@Scheduled(cron = "0 0 1 * * ?", zone = "GMT+12")
		public void updateCustomerStatus() {
			customerService.updateCustomerStatus();
		}
		
		@Scheduled(cron = "0 0 2 * * ?", zone = "GMT+12")
		public void shareCustomerStatusToABMF() {
			customerService.shareCustomerStatus();
		}
		
		@Scheduled(cron = "0 0 3 * * ?", zone = "GMT+12")
		public void clearExpiryCart() {
			cartService.clearCartByExpireDate();
		}
		
		@Scheduled(cron = "0 0 0 1 * ?", zone = "GMT+12")  //every month 1 date
		public void updatePostpaidAmountwithLateCharge() {
			paymentService.updatePostpaidAmountWithLateCharge();
		}*/
}
