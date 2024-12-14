package com.wpits.entities;

import java.time.LocalDateTime;
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
@Table(name = "entity")
@Getter
@Setter
public class Entitys {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "external_id")
	private String externalId;
	
	@Column(name = "description", nullable = false)
	private String description;
	
    @Column(name = "create_datetime", nullable = false)
    private LocalDateTime createDatetime;
	
	@Column(name = "optlock", nullable = false)
	private int optlock;
	
    @Column(name = "deleted", nullable = false)
    private Integer deleted;

    @Column(name = "invoice_as_reseller", nullable = false)
    private Boolean invoiceAsReseller;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;
    
    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;
    
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Entitys parentEntitys;
    
    @OneToMany(mappedBy = "entitys",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderPeriod> orderPeriod;
    
    @OneToMany(mappedBy = "entitys", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountType> accountTypes;
    
    @OneToMany(mappedBy = "entitys", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BaseUser> baseUsers;
    
    @OneToMany(mappedBy = "entitys",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<BillingProcess> billingProcesses;
    
	/*
	 * @OneToMany(mappedBy = "entitys", cascade = CascadeType.ALL, fetch =
	 * FetchType.LAZY) private List<MetaFieldName> metaFieldNames;
	 */
}
