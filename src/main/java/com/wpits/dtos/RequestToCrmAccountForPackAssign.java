package com.wpits.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestToCrmAccountForPackAssign {

	private String first_name;
	private String last_name;
	private String ekyc_status;
	private String ekyc_token;
	private LocalDateTime ekyc_date;
	private int customer_id;
	private String customer_type;
	private String msisdn;
	private String imsi;
	private Integer pack_id;
	private Boolean payment_status;
	private int parent_customer_id;
}
