package com.wpits.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntitysDto {

	private int id;
	private String externalId;
	private String description;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createDatetime;
	private int optlock;
	private Integer deleted;
	private Boolean invoiceAsReseller;
	private CurrencyDto currency;
	private LanguageDto language;
	private EntitysDto parentEntitys;
}
