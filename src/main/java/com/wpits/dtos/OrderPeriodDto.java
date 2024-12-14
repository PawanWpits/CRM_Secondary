package com.wpits.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPeriodDto {

	private int id;
	private int value;
	private int optlock;
	private EntitysDto entitys;
	private PeriodUnitDto periodUnit;
}
