package com.wpits.dtos;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EkycCrmMappingDto {

	private int id;

	private String ekycId;

	private String idDocId;

	private String address;

	private String firstName;

	private String maidenName;
	
	private String lastName;
	
	private String gender;
	
	private String dob;

	private String nationality;

	private String rechargeType;

	private String userType;

	private String residentType;

	private String token;

	private String originalPhotoUrl;

	private String msisdn;
	
	private String CreateTime;
	
	private String alternateNumber;
	
	private String city;
	
	private String countryCode;
	
	private String email;
	
	private int invoiceChild;
	
	private int isParent;
	
	private Double monthlyLimit;
	
	private int nextInvoiceDayOfPeriod;
	
	private LocalDate nextInoviceDate;
	
	private String invoiceDesign;
	
	private int notificationInclude;
	
	private int parentId;
	
	private String personInitial;
	
	private String personTitle;
	
	private Double rechargeThreshold;
	
	private int referralFeePaid;
	
	private int partnerId;
	
	private String electricityMeterId;
	
	private String Status;
	
	private String serviceType;
	
	private String remark;
	
	private boolean isVip;
}
