package com.wpits.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerPayoutDto {

	private int id;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate startingDate;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate endingDate;
	
	private Double paymentsAmount;
	
	private Double refundsAmount;
	
	private Double balanceLeft;
	
	private int paymentId;
	
	private int optlock;
	
	private PartnerDto partner;
}
