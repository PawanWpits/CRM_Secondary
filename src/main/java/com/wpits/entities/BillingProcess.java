package com.wpits.entities;

import java.time.LocalDate;
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
@Table(name = "billing_process")
@Getter
@Setter
public class BillingProcess {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "billing_date", nullable = false)
	private LocalDate billingDate;
		
	@Column(name = "period_value", nullable = false)
	private int periodValue;
	
	@Column(name = "is_review", nullable = false)
	private int isReview;
	
	@Column(name = "retries_to_do",nullable = false)
	private int retriesToDo;
	
	@Column(name = "optlock",nullable = false)
	private int optlock;
	
	@ManyToOne
	@JoinColumn(name = "period_unit_id",nullable = false)
	private PeriodUnit periodUnit;
	
	@ManyToOne
	@JoinColumn(name = "entity_id",nullable = false)
	private Entitys entitys;
	
	@ManyToOne
	@JoinColumn(name = "paper_invoice_batch_id")
	private PaperInvoiceBatch paperInvoiceBatch;
	
	@OneToMany(mappedBy = "billingProcess",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Invoice> invoices;
	
}
