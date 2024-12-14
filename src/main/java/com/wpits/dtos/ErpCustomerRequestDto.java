package com.wpits.dtos;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErpCustomerRequestDto {

	@Size(min = 3, message = "first name must be at least 3 characters long")
	private String firstName;
	
	@NotBlank(message = "Please provide a lastname")
	private String lastName;
	
	@Pattern(regexp = "^(.+)@(.+)$",message = "Please provide a valid email address !!")
    @NotBlank(message = "email is required !!")
	private String email;
	
	@NotBlank(message = "Please provide a date of birth")
	private String dateOfBirth;
	
	@Size(min = 1, message = "gender must be at least 1 characters")
	private String gender;
	
	@NotBlank(message = "Please provide a marital status")
	private String maritalStatus;

	@Size(min = 8, message = "street addres1 must be at least 8 characters long")
	private String streetAddres1;

	private String streetAddres2;

	@NotBlank(message = "Please provide a city")
	private String city;
	
	@NotBlank(message = "Please provide a state province")
	private String stateProvince;

	@NotBlank(message = "Please provide a postal code")
	private String postalCode;

	@NotBlank(message = "Please provide a country code")
	private String countryCode;
	
	//@NotBlank(message = "Please provide a person title")
	private String personTitle;

	@Min(value = 3, message = "phone country code should not be less than 3")
	private int phoneCountryCode;

	@Size(min = 10, message = "phone phone number must be at least 10 characters long")
	private String phonePhoneNumber;
	
	private String alternateNumber;

	private String landlineNumber;
}
