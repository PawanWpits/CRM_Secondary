package com.wpits.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.CustomerDetailWithPack;
import com.wpits.dtos.CustomerDto;
import com.wpits.dtos.ErpCustomerRequestDto;
import com.wpits.dtos.ImageResponse;
import com.wpits.dtos.PartnerSaleReportResponse;
import com.wpits.dtos.ResponseMessage;

public interface CustomerService {

	CustomerDto createCustomer(CustomerDto customerDto, int accountId, int invoiceDeliveryId, int partnerId,int baseUserId, int orderPeriodId, String msisdn,  int deviceInventoryId,int routerSerialNo);
	
	CustomerDto updateCustomer(CustomerDto customerDto,int customerId);
	
	List<CustomerDto> getAllCustomers(Integer pageNo, Integer pageSize, String sortBy);
	
	CustomerDto getSingleCustomer(int customerId);
	
	void deleteCustomer(int customerId);
	
	List<CustomerDetailWithPack> getAllCustomerDetailWithPack();
	
	CustomerDto getCustomerByMsisdn(String msisdn);
	
	PartnerSaleReportResponse getPartnerProductSaleReport(int partnerId);
	
	void checkPostpaidCustomerPayment();
	
	void updateCustomerStatus();  //Schedular
	
	List<CustomerDto> searchByKeyword(String keyword);

	List<CustomerDto> searchCustomers(String keyword);
	
	void shareCustomerStatus();  //Schedular
	
	void updateMonthlyAmountByPackAssign(String msisdn, double amount);
	
	CustomerDto updateCustomerByField(int customerId, Map<String, Object> fields);
	
	ImageResponse updateCustomerImage(int customerId, MultipartFile image) throws IOException;
	
	ResponseMessage createErpCustomer(ErpCustomerRequestDto erpCustomerRequestDto);
	
	void addChildSimWithParent(String parentMsisdn, Map<String, String> childNumbers);
	
	ApiResponseMessage addChildNumberwithParnet(String parentMsisdn, String childMsisdn);

	List<CustomerDto> getAllChildAccountsOfParent(String msisdn);
	
	ResponseMessage updateErpCustomer(int customerId, Map<String, Object> fields);
	
}
