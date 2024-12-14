package com.wpits.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ekyc_activate_in_crm")
@Getter
@Setter
public class EkycCrmMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "ekyc_id")
	private String ekycId;
	
	@Column(name = "id_doc_id")
	private String idDocId;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "maiden_name")
	private String maidenName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "dob")
	private String dob;
	
	@Column(name = "nationality")
	private String nationality;
	
	@Column(name = "recharge_type")
	private String rechargeType;
	
	@Column(name = "user_type")
	private String userType;
	
	@Column(name = "resident_type")
	private String residentType;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "original_photo_url")
	private String originalPhotoUrl;
	
	@Column(name = "msisdn",nullable = false,unique = true)
	private String msisdn;
	
	@Column(name = "Create_time")
	private String CreateTime;
	//
	@Column(name = "alternate_number")
	private String alternateNumber;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "country_code")
	private String countryCode;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "invoice_child")
	private int invoiceChild;

	
	@Column(name = "is_parent")
	private int isParent;
	
	@Column(name = "monthly_limit")
	private Double monthlyLimit;
	
	@Column(name = "next_invoice_day_of_period")
	private int nextInvoiceDayOfPeriod;
	
	@Column(name = "next_inovice_date")
	private LocalDate nextInoviceDate; 
	
	@Column(name = "invoice_design")
	private String invoiceDesign;
	
	@Column(name = "notification_include")
	private int notificationInclude;
	
	@Column(name = "parent_id")
	private int parentId;
	
	@Column(name = "person_initial")
	private String personInitial;
	
	@Column(name = "person_title")
	private String personTitle;
	
	@Column(name = "recharge_threshold")
	private Double rechargeThreshold;
	
	@Column(name = "referral_fee_paid")
	private int referralFeePaid;
	
	@Column(name = "partner_id")
	private int partnerId;
	
	@Column(name = "electricity_meter_id")
	private String electricityMeterId;
	
	@Column(name = "Status")
	private String Status;
	
	@Column(name = "service_type", nullable = false)
	private String serviceType;
	
	@Column(name = "remark")
	private String remark;
	
	private boolean isVip;
}
