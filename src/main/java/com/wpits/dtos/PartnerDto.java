package com.wpits.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerDto {

	private int id;

	private Double totalPayments;

	private Double totalRefunds;

	private Double totalPayouts;

	private Double duePayout;

	private int optlock;

	private String type;

	private int parentId;

	private String commissionType;
	
//	
	private String fristName;
	
	private String lastName;
	
	private String email;
	
	private String businessAddress;
	
	private String businessNature;
	
	private String contact;
	
	private String documentId;
	
	private String documentType;
	
	private String token;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate creationDate;
	
	private String locallity;
	
	private String coordinate;
	
	private String reasonStatus;
	
	private Boolean isActive;
//	
	private BaseUserDto baseUser;
	
	private PartnerCommissionDto partnerCommission;
}
