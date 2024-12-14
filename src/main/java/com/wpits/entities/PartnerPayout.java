package com.wpits.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "partner_payout")
@Getter
@Setter
public class PartnerPayout {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "starting_date", nullable = false)
	private LocalDate startingDate;
	
	@Column(name = "ending_date", nullable = false)
	private LocalDate endingDate;
	
	@Column(name = "payments_amount", nullable = false)
	private Double paymentsAmount;
	
	@Column(name = "refunds_amount", nullable = false)
	private Double refundsAmount;
	
	@Column(name = "balance_left",nullable = false)
	private Double balanceLeft;
	
	@Column(name = "payment_id")
	private int paymentId;
	
	@Column(name = "optlock",nullable = false)
	private int optlock;
	
	@ManyToOne
	@JoinColumn(name = "partner_id")
	private Partner partner;
}
