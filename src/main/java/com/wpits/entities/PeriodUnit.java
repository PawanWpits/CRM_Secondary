package com.wpits.entities;

import java.util.List;

import javax.persistence.CascadeType;
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
@Table(name = "period_unit")
@Getter
@Setter
public class PeriodUnit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToMany(mappedBy = "periodUnit", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<OrderPeriod> orderPeriod;
	
	@OneToMany(mappedBy = "periodUnit",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<BillingProcess> billingProcesses;
}
