package com.wpits.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorDto {

	private int id;
	
	private String firstName;
	
	private String maidenName;
	
	private String lastName;
	
	private String email;
	
	private String contact;
	
	private String address;
	
	private String companyName;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate createDate;
	
	private String token;
	
	private int userId;
}
