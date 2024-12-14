package com.wpits.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {

	private int id;
	
	private String token;
	
	private int deviceId; 
	
	private Double amount;
	
	private int partnerId;
	
	private String type;
	
	private String name;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate createDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate expiryDate;
}
