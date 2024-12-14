package com.wpits.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wpits.config.TopicConstent;
import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.CustomerDetailWithPack;
import com.wpits.dtos.CustomerDto;
import com.wpits.dtos.ErpCustomerRequestDto;
import com.wpits.dtos.ImageResponse;
import com.wpits.dtos.PartnerSaleReportResponse;
import com.wpits.dtos.ResponseMessage;
import com.wpits.services.CustomerService;
import com.wpits.services.FileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
@Api(value = "UserController", description = "This APIs related to perform customer operations !!")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${customer.profile.image.path}")
	private String imageUploadPath;
	
	
	
	  @Autowired 
	  private KafkaTemplate<String, CustomerDto> kafkaTemplate;
	 
	 
	
	@PostMapping("/savecustomer/account/{accountId}/invoice/{invoiceDeliveryId}/baseuser/{baseUserId}/orderperiod/{orderPeriodId}")
	@ApiOperation(value = "create new customer !!")
	@ApiResponses(value = {
            @ApiResponse(code = 200 ,message = "Success | Ok"),
            @ApiResponse(code = 401 , message = "not authorized !!"),
            @ApiResponse(code = 201 ,message = "new customer created !!"),
    })
	public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto,@PathVariable int accountId,@PathVariable int invoiceDeliveryId,@PathVariable int baseUserId,@PathVariable int orderPeriodId,
			@RequestParam(value = "msisdn", required = false) String msisdn,
			@RequestParam(value = "device", defaultValue = "0") int deviceInventoryId,
			@RequestParam(value = "partner", defaultValue = "0") int partnerId,
			@RequestParam(value = "router", defaultValue = "0") int routerSerialNo){ //not pass router serial no from KYC take by cart router
		
		CustomerDto customer = customerService.createCustomer(customerDto, accountId, invoiceDeliveryId, partnerId, baseUserId,orderPeriodId, msisdn, deviceInventoryId,routerSerialNo);
		
		System.out.println("Kafka sending...");
		kafkaTemplate.send(TopicConstent.CRM_TOPIC_NAME_SAVE_CUSTOMER, customer);
		System.out.println("kafka sent !!");

		return new ResponseEntity<CustomerDto>(customer, HttpStatus.CREATED);
	}

	@PutMapping("/updatecustomer/{customerId}")
	public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerDto customerDto,@PathVariable int customerId){
		return ResponseEntity.ok(customerService.updateCustomer(customerDto, customerId));
	}
	
	@GetMapping("/customers")
	@ApiOperation(value = "get all customers",tags = {"customer-controller"})
	public ResponseEntity<List<CustomerDto>> getAllCustomers(
			@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy ){
		return ResponseEntity.ok(customerService.getAllCustomers(pageNo, pageSize, sortBy));
	}
	
	@GetMapping("/customer/{customerId}")
	@ApiOperation(value = "get single customer by customerId !!")
	public ResponseEntity<CustomerDto> singleCustomer(@PathVariable int customerId){
		return ResponseEntity.ok(customerService.getSingleCustomer(customerId));
	}
	
	@DeleteMapping("/deletecustomer/{customerId}")
	public ResponseEntity<ApiResponseMessage> deleteCustomer(@PathVariable int customerId){
		customerService.deleteCustomer(customerId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
	//upload image
	@PostMapping("/saveimage/{customerId}")
	public ResponseEntity<ImageResponse> uploadCustomerImage(@RequestParam("customerImage") MultipartFile image, @PathVariable int customerId) throws IOException{		
		String imageName = fileService.uploadFile(image, imageUploadPath);
		
		CustomerDto customer = customerService.getSingleCustomer(customerId);
		customer.setImageName(imageName);
		customerService.updateCustomer(customer, customerId);
		
		
		ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message("image uploaded").success(true).status(HttpStatus.CREATED).build();
		return new ResponseEntity<ImageResponse>(imageResponse,HttpStatus.CREATED);
	}
	
	//serve image
	
	@GetMapping("/image/{customerId}")
	public void serveImage(@PathVariable int customerId, HttpServletResponse response) throws IOException {
		
		CustomerDto customer = customerService.getSingleCustomer(customerId);
		System.out.println("@@@ imageUrl"+imageUploadPath+customer.getImageName());
		InputStream resource = fileService.getResource(imageUploadPath, customer.getImageName());
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		StreamUtils.copy(resource, response.getOutputStream());
	}
		
	//customer with pack detail
	
	@GetMapping("/customers/pack/details")
	public ResponseEntity<List<CustomerDetailWithPack>> getCustomerDetailsWithPack() throws IOException{	
		return ResponseEntity.ok(customerService.getAllCustomerDetailWithPack());
	}
	
	@GetMapping("/customerbymsisdn/{msisdn}")
	public ResponseEntity<CustomerDto> getCustomerByMsisdn(@PathVariable String msisdn){
		return ResponseEntity.ok(customerService.getCustomerByMsisdn(msisdn));
	}
	
	@GetMapping("/partner/product/sell/report/{partnerId}")
	public ResponseEntity<PartnerSaleReportResponse> partnerProductSaleReport(@PathVariable int partnerId){
		return ResponseEntity.ok(customerService.getPartnerProductSaleReport(partnerId));
	}

	@GetMapping("/postpaid/customer/payment/cron/job")
	public ResponseEntity<String> PostpaidCustomerPaymentInfo(){
		customerService.checkPostpaidCustomerPayment();
		return ResponseEntity.ok("Ok");
	}
	
	//
	@GetMapping("/status")
	public void updateCustomerStatus() {
		customerService.updateCustomerStatus();
	}
	
	@GetMapping("/customers/keyword/{keyword}")
	public ResponseEntity<List<CustomerDto>> searchByKeyword(@PathVariable String keyword){
		return ResponseEntity.ok(customerService.searchByKeyword(keyword));
	}


	@GetMapping("/search/customers")
    public ResponseEntity<List<CustomerDto>> searchCustomers(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(customerService.searchCustomers(keyword));
    }
	
	
	@GetMapping("/status/abmf")
	public void shareCustomerStatusToABMF() {
		customerService.shareCustomerStatus();
	}
	
	//set PostPaid monthly amount and nextInvoiceDate
	@PostMapping("/set/monthly/amount/msisdn/{msisdn}/amount/{amount}")
	public ResponseEntity<ApiResponseMessage> setMonthlyAmountByPackAssignTime(@PathVariable String msisdn, @PathVariable double amount){
		customerService.updateMonthlyAmountByPackAssign(msisdn, amount);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("successful !!").success(true).status(HttpStatus.OK).build());
	}
	
	@PatchMapping("/partial/update/customer/{customerId}")
	public ResponseEntity<CustomerDto> updateCustomeByField(@PathVariable  int customerId,@RequestBody Map<String, Object> fields){
		return ResponseEntity.ok(customerService.updateCustomerByField(customerId, fields));
	}
	
	@PutMapping("/update_image/{customerId}")
	public ResponseEntity<ImageResponse> updateCustomerImage(@PathVariable  int customerId, @RequestParam("customerImage") MultipartFile image) throws IOException{
		
		return ResponseEntity.ok(customerService.updateCustomerImage(customerId, image));
	}
	
	@PostMapping("/save_customer")
	public ResponseEntity<ResponseMessage> createErpCustomer(@RequestBody @Valid ErpCustomerRequestDto erpCustomerRequestDto){
		
		ResponseMessage createErpCustomer = customerService.createErpCustomer(erpCustomerRequestDto);
		
		if (createErpCustomer.getStatus() == HttpStatus.CREATED) {
			
			return new ResponseEntity<ResponseMessage>(createErpCustomer,HttpStatus.CREATED);
		} else {

			return new ResponseEntity<ResponseMessage>(createErpCustomer,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PutMapping("/map/parent_number/{parentNumber}/child_number/{childNumber}")
	public ResponseEntity<ApiResponseMessage> addChildConnectionWithParent(@PathVariable String parentNumber, @PathVariable String childNumber){
		
		return ResponseEntity.ok(customerService.addChildNumberwithParnet(parentNumber, childNumber));
	}
	
	@GetMapping("/child_account_of_parent/{msisdn}")
	public ResponseEntity<List<CustomerDto>> getAllChildAccountsOfParent(@PathVariable String msisdn){
		return ResponseEntity.ok(customerService.getAllChildAccountsOfParent(msisdn));
	}
	
	@PatchMapping("/update_customer/{customerId}")
	public ResponseEntity<ResponseMessage> updateErpCustomerPartially(@PathVariable int customerId, @RequestBody Map<String, Object> fields){
		return ResponseEntity.ok(customerService.updateErpCustomer(customerId, fields));
	}
}
