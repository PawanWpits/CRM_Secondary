package com.wpits.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contact")
@Getter
@Setter
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
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
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "create_datetime", nullable = false)
	private LocalDateTime createDateTime;
	
	@Column(name = "deleted", nullable = false)
	private int deleted;
	
	@Column(name = "notification_include")
	private int notificationInclude;
	
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "optlock", nullable = false)
	private int optlock;
	
	
}
