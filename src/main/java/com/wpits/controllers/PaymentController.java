package com.wpits.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.PaymentDto;
import com.wpits.services.PaymentService;

@RestController
@RequestMapping("/api")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/savepayment/currency/{currencyId}/paymentrsult/{paymentResultId}/paymentmethod/{paymentMethodId}")	
	public ResponseEntity<ApiResponseMessage> savePayment(@RequestBody PaymentDto paymentDto, @PathVariable int currencyId, @PathVariable int paymentResultId, @PathVariable int paymentMethodId,
			@RequestParam(value = "creditCard", defaultValue = "0") int creditCardId,
			@RequestParam(value = "msisdn", required = false) String msisdn){
		return new ResponseEntity<ApiResponseMessage>(paymentService.createPayment(paymentDto, currencyId, paymentResultId, paymentMethodId, creditCardId,msisdn),HttpStatus.CREATED);
	}
	
	@PutMapping("/updatepayment/{paymentId}")
	public ResponseEntity<PaymentDto> updatePayment(@RequestBody PaymentDto paymentDto, @PathVariable int paymentId){
		return ResponseEntity.ok(paymentService.updatepayment(paymentDto, paymentId));
	}
	
	@GetMapping("/payments")
	public ResponseEntity<List<PaymentDto>> getAllPayment(){
		return ResponseEntity.ok(paymentService.getAllPayment());
	}
	
	@GetMapping("/payment/{paymentId}")
	public ResponseEntity<PaymentDto> singlePayment(@PathVariable int paymentId){
		return ResponseEntity.ok(paymentService.getByIdPayment(paymentId));
	}
	
	@DeleteMapping("/deletepayment/{paymentId}")
	public ResponseEntity<ApiResponseMessage> deletePayment(@PathVariable int paymentId){
		paymentService.deletePayment(paymentId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
	@GetMapping("/payment/customer/{custId}")
	public ResponseEntity<PaymentDto> getPaymentDetailByCustId(@PathVariable int custId){
		return ResponseEntity.ok(paymentService.getPaymentRecordByCustId(custId));
	}
	
	//postpaid monthly bill payment
	@PostMapping("/postpaid/bill/payment/invoiceNumber/{invoiceNumber}/amount/{amount}/currency/{currencyId}/paymentrsult/{paymentResultId}/paymentmethod/{paymentMethodId}")	
	public ResponseEntity<ApiResponseMessage> savePayment(@PathVariable String invoiceNumber, @PathVariable Double amount, @PathVariable int currencyId, @PathVariable int paymentResultId, @PathVariable int paymentMethodId,
			@RequestParam(value = "creditCard", defaultValue = "0") int creditCardId,
			@RequestParam(value = "partner", defaultValue = "0") int partnerId,
			@RequestParam(value = "transactionId", required = false) String transactionId){
		return new ResponseEntity<ApiResponseMessage>(paymentService.postPaidBillPayment(invoiceNumber, amount, currencyId, paymentResultId, paymentMethodId, creditCardId, partnerId, transactionId),HttpStatus.CREATED);
	}
	
	//Scheduler just for test
	@PutMapping("/test/postpaid_amount/with_late_charge")
	public void updatePostpaidAmountwithLateCharge(){
		
		paymentService.updatePostpaidAmountWithLateCharge();
		/*return ResponseEntity.ok();*/
	}
	
}
