package com.wpits.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDto {

	private int id;

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

	private int userId;

	private int optlock;
}
