package com.wpits.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "base_user")
@Getter
@Setter
public class BaseUser implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "deleted", nullable = false)
	private int deleted;
	
	@Column(name = "subscriber_status")
	private int subscriberStatus;
	
	@Column(name = "create_datetime", nullable = false)
	private LocalDateTime createDateTime;
	
	@Column(name = "last_status_change")
	private LocalDateTime lastStatusChange;
	
	@Column(name = "last_login")
	private LocalDateTime lastLogin;
	
	@Column(name = "user_name",unique = true, nullable = false)
	private String email;       //Email=userName
	
	private int roleId;
	
	@Column(name = "failed_attempts", nullable = false)
	private int failedAttempts;
	
	@Column(name = "optlock", nullable = false)
	private int optlock;

	@Column(name = "change_password_date")
	private LocalDate changePasswordDate;
	
	@Column(name = "encryption_scheme", nullable = false)
	private int encryptionScheme;
	
	@Column(name = "account_locked_time")
	private LocalDateTime accountLockedTime;

	@Column(name = "account_disabled_date")
	private LocalDate accountDisabledDate;
	
	@ManyToOne
	@JoinColumn(name = "entity_id")
	private Entitys entitys;
	
	@ManyToOne
	@JoinColumn(name = "language_id")
	private Language language;
	
	@ManyToOne
	@JoinColumn(name = "currency_id")
	private Currency currency;
	
	@ManyToOne
	@JoinColumn(name = "status_id")
	private UserStatus userStatus;
	
	@OneToMany(mappedBy = "baseUser",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Partner> partner;
	
	@OneToMany(mappedBy = "baseUser",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Customer> customers;
	
	
	//role
	  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)  
	  @JoinTable(name = "base_user_role", joinColumns = @JoinColumn(name ="base_user", referencedColumnName = "id"), 
	  inverseJoinColumns = @JoinColumn(name ="role", referencedColumnName = "id")) 
	  private Set<Role> roles = new HashSet<Role>();


	  @Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			List<SimpleGrantedAuthority> authories = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
			return authories;
		}


		@Override
		public String getUsername() {
			return email;
		}


		@Override
		public boolean isAccountNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}


		@Override
		public boolean isAccountNonLocked() {
			return true;
		}


		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}


		@Override
		public boolean isEnabled() {
			return true;
		}
		 
}
