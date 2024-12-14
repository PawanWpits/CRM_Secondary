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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "paper_invoice_batch")
@Getter
@Setter
public class PaperInvoiceBatch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "total_invoices",nullable = false)
	private int totalInvoices;
	
	@Column(name = "delivery_date")
	private LocalDate deliveryDate;
	
	@Column(name = "is_self_managed",nullable = false)
	private int isSelfManaged;
	
	@Column(name = "optlock",nullable = false)
	private int optlock;
	
	@OneToMany(mappedBy = "paperInvoiceBatch",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<BillingProcess> billingProcesses;
	
	@OneToMany(mappedBy = "paperInvoiceBatch",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Invoice> invoices;
}
