package com.wpits.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wpits.entities.AccountType;
import com.wpits.entities.BaseUser;
import com.wpits.entities.Customer.Status;
import com.wpits.entities.DeviceInventory;
import com.wpits.entities.InvoiceDeliveryMethod;
import com.wpits.entities.OrderPeriod;
import com.wpits.entities.Partner;
import com.wpits.entities.SimInventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDetailWithPack {

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

	@JsonFormat(pattern="yyyy-MMM-dd")
	private LocalDate nextInoviceDate;

	private String invoiceDesign;

	private Double creditNotificationLimit1;

	private Double creditNotificationLimit2;

	private Double rechargeThreshold;

	private Double monthlyLimit;

	private Double currentMonthlyAmount;

	@JsonFormat(pattern = "yyyy-MMM-dd HH:mm:ss")
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

	@JsonFormat(pattern = "yyyy-MMM-dd HH:mm:ss")
	private LocalDateTime createDateTime;

	private int deleted;

	private int notificationInclude;
	
	private Status serviceStatus;

	// add field given by Sangmesh sir
	//private SimInventory SimInventory;   if getting exception try this instead of below
	private SimInventory simInventory;

	private String customerType;

	private String gender;

	private String ekycStatus;

	private String ekycToken;

	@JsonFormat(pattern = "yyyy-MMM-dd HH:mm:ss")
	private LocalDateTime ekycDate;

	private String alternateNumber;

	private String landlineNumber;

	private String dateOfBirth;

	private String vatId;

	private String profession;

	private String maritalStatus;
	
	private String imageName; 

	private int accountType;

	private int invoiceDeliveryMethod;

	private int partner;

	private int baseUser;

	private int orderPeriod;
	
	private int deviceInventory;
	
	private String pack_name;
	
	private String activation_date;
	
	private String expiration_date;
}
