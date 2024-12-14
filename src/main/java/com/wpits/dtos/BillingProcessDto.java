package com.wpits.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingProcessDto {

	private int id;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate billingDate;
	private int periodValue;
	private int isReview;
	private int retriesToDo;
	private int optlock;
	private PeriodUnitDto periodUnit;
	private EntitysDto entitys;
	private PaperInvoiceBatchDto paperInvoiceBatch;
}
