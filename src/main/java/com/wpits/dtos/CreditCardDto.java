package com.wpits.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditCardDto {

	private int id;
	
	private String ccNumber;
	
	private String ccNumberPlain;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate ccExpiry;
	
	private String name;
	
	private int ccType;
	
	private int deleted =0;
	
	private String gatewayKey;
	
	private int optlock;
}
