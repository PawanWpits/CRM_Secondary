package com.wpits.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wpits.entities.Customer.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {

	private int id;

	private int referralFeePaid;

	private int autoPaymentType;

	private int dueDateUnitId;

	private int dueDateValue;

	private int dfFm;

	private int parentId;

	private int isParent;

	private int excludeAging;

	private int invoiceChild;

	private int optlock;

	private Double dynamicBalance;

	private Double creditLimit;

	private Double autoRecharge;

	private Boolean useParentPricing;

	private int nextInvoiceDayOfPeriod;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate nextInoviceDate;

	private String invoiceDesign;

	private Double creditNotificationLimit1;

	private Double creditNotificationLimit2;

	private Double rechargeThreshold;

	private Double monthlyLimit;

	private Double currentMonthlyAmount;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime currentMonth;

	// contct
	private String organizationName;

	private String streetAddres1;

	private String streetAddres2;

	private String city;

	private String stateProvince;

	private String postalCode;

	private String countryCode;

	private String lastName;

	private String firstName;

	private String personInitial;

	private String personTitle;

	private int phoneCountryCode;

	private int phoneAreaCode;

	private String phonePhoneNumber;

	private int faxCountryCode;

	private int faxAreaCode;

	private String faxPhoneNumber;

	private String email;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createDateTime;

	private int deleted;

	private int notificationInclude;
	
//	private Boolean paymentStatus;

	// add field given by Sangmesh sir
	//private SimInventory SimInventory;   if getting exception try this instead of below
	private SimInventoryDto simInventory;

	private String customerType;

	private String gender;

	private String ekycStatus;

	private String ekycToken;
//ekyc	
	private String ekycId;

	private String idDocId;
	
	private String userType;

	private String residentType;
	
	private String originalPhotoUrl;
	
	private String maidenName;
	
	private String nationality;
	
	private String remark;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime ekycDate;

	private String alternateNumber;

	private String landlineNumber;

	private String dateOfBirth;

	private String vatId;

	private String profession;

	private String maritalStatus;
	
	private String imageName; 

	private AccountTypeDto accountType;

	private InvoiceDeliveryMethodDto invoiceDeliveryMethod;

	private PartnerDto partner;

	private BaseUserDto baseUser;

	private OrderPeriodDto orderPeriod;
	
	private DeviceInventoryDto deviceInventory;
	
	private String electricityMeterId;
	
	private Status serviceStatus;
	
	private String serviceType;
	
	private String payment;
	
	private LocalDateTime updationTime;
	
	private boolean isVip;
	
	private RouterInventoryDto routerInventory;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime childCreationTime;
}
