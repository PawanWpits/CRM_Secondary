package com.wpits.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer")
@Getter
@Setter
public class Customer {

	public enum Status{
		ACTIVE, BLOCKED, SUSPEND, DEACTIVATE;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "referral_fee_paid")
	private int referralFeePaid;
	
	@Column(name = "auto_payment_type")
	private int autoPaymentType;
	
	@Column(name = "due_date_unit_id")
	private int dueDateUnitId;
	
	@Column(name = "due_date_value")
	private int dueDateValue;
	
	@Column(name = "df_fm")
	private int dfFm;
	
	@Column(name = "parent_id")
	private int parentId;
	
	@Column(name = "is_parent")
	private int isParent;
	
	@Column(name = "exclude_aging", nullable = false)
	private int excludeAging;
	
	@Column(name = "invoice_child")
	private int invoiceChild;
	
	@Column(name = "optlock", nullable = false)
	private int optlock;
	
	@Column(name = "dynamic_balance")
	private Double dynamicBalance;
	
	@Column(name = "credit_limit")
	private Double creditLimit;
	
	@Column(name = "auto_recharge")
	private Double autoRecharge;
	
	@Column(name = "use_parent_pricing",nullable = false)
	private Boolean useParentPricing;
	
	@Column(name = "next_invoice_day_of_period")
	private int nextInvoiceDayOfPeriod;
	
	@Column(name = "next_inovice_date")
	private LocalDate nextInoviceDate; 
	
	@Column(name = "invoice_design")
	private String invoiceDesign;
	
	@Column(name = "credit_notification_limit1")
	private Double creditNotificationLimit1;
	
	@Column(name = "credit_notification_limit2")
	private Double creditNotificationLimit2;
	
	@Column(name = "recharge_threshold")
	private Double rechargeThreshold;
	
	@Column(name = "monthly_limit")
	private Double monthlyLimit;
	
	@Column(name = "current_monthly_amount")
	private Double currentMonthlyAmount;
	
	@Column(name = "current_month")
	private LocalDateTime currentMonth;
	
	//contact
	@Column(name = "organization_name")
	private String organizationName;
	
	@Column(name = "street_addres1")
	private String streetAddres1;
	
	@Column(name = "street_addres2")
	private String streetAddres2;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state_province")
	private String stateProvince;
	
	@Column(name = "postal_code")
	private String postalCode;
	
	@Column(name = "country_code")
	private String countryCode;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "person_initial")
	private String personInitial;
	
	@Column(name = "person_title")
	private String personTitle;
	
	@Column(name = "phone_country_code")
	private int phoneCountryCode;
	
	@Column(name = "phone_area_code")
	private int phoneAreaCode;
	
	@Column(name = "phone_phone_number")
	private String phonePhoneNumber;
	
	@Column(name = "fax_country_code")
	private int faxCountryCode;
	
	@Column(name = "fax_area_code")
	private int faxAreaCode;
	
	@Column(name = "fax_phone_number")
	private String faxPhoneNumber;
	
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@Column(name = "create_datetime", nullable = false)
	private LocalDateTime createDateTime;
	
	@Column(name = "deleted", nullable = false)
	private int deleted;
	
	@Column(name = "notification_include")
	private int notificationInclude;
	
	//add field given by Sangmesh sir
	
	@OneToOne(cascade = CascadeType.ALL) 
	@JoinColumn(name = "msisdn",referencedColumnName = "msisdn",unique = true)
	private SimInventory simInventory; 
	
	@Column(name = "customer_type")
	private String customerType;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "ekyc_status")
	private String ekycStatus;
	
	@Column(name = "ekyc_token")
	private String ekycToken;
	
	@Column(name = "ekyc_date")
	private LocalDateTime ekycDate;

// ekyc
	@Column(name = "ekyc_id")
	private String ekycId;
	
	@Column(name = "id_doc_id")
	private String idDocId;

	@Column(name = "user_type")
	private String userType;

	@Column(name = "resident_type")
	private String residentType;

	@Column(name = "original_photo_url")
	private String originalPhotoUrl;

	@Column(name = "maiden_name")
	private String maidenName;
	
	@Column(name = "nationality")
	private String nationality;
	
	@Column(name = "remark")
	private String remark;

	@Column(name = "alternate_number")
	private String alternateNumber;

	@Column(name = "landline_number")
	private String landlineNumber;
	
	@Column(name = "date_of_birth")
	private String dateOfBirth;
	
	@Column(name = "vat_id")
	private String vatId;
	
	@Column(name = "profession")
	private String profession;
	
	@Column(name = "marital_status")
	private String maritalStatus;
	
	@Column(name = "cust_image_name")
	private String imageName;
	
//	@Column(name = "payment_status")
//	private Boolean paymentStatus;
	  
	@ManyToOne
	@JoinColumn(name = "account_type_id")
	private AccountType accountType;
	
	@ManyToOne
	@JoinColumn(name = "invoice_delivery_method_id")
	private InvoiceDeliveryMethod invoiceDeliveryMethod;
	
	@ManyToOne
	@JoinColumn(name = "partner_id")
	private Partner partner;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private BaseUser baseUser;
	
	@ManyToOne
	@JoinColumn(name = "main_subscript_order_period_id")
	private OrderPeriod orderPeriod;
	
	@ManyToOne
	@JoinColumn(name = "device_inventory_id", unique = true)
	private DeviceInventory deviceInventory;
	
	@Column(name = "electricity_meter_id")
	private String electricityMeterId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "service_status")
	private Status serviceStatus;
	
	@Column(name = "service_type",nullable = false)
	private String serviceType;
	
	private String payment;
	
	private LocalDateTime updationTime;
	
    @Column(name = "is_vip", nullable = false)
    private boolean isVip;
    
    @ManyToOne
    @JoinColumn(name = "router_id", unique = true)
    private RouterInventory routerInventory;
    
    private LocalDateTime childCreationTime;

}
