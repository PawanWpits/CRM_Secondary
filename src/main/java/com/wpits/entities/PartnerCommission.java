package com.wpits.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "partner_commission")
@Getter
@Setter
public class PartnerCommission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "partner_id")
	private int partnerId;
	
	@Column(name = "commission_process_run_id")
	private int commissionProcessRunId;
	
	@ManyToOne
	@JoinColumn(name = "currency_id")
	private Currency currency;
	
	@OneToMany(mappedBy = "partnerCommission", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Partner> partners;

}
