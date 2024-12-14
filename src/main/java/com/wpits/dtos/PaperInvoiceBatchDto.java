package com.wpits.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaperInvoiceBatchDto {

	private int id;
	private int totalInvoices;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate deliveryDate;
	private int isSelfManaged;
	private int optlock;
}
