package com.wpits.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseUserDto {

	private int id;
	
	@Size(min = 2, max = 20, message = "Invalid name !!")
	private String name;
	
	@Size(min = 2, max = 15, message = "Invalid password !!")
	private String password;

	private int deleted;

	private int subscriberStatus;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createDateTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastStatusChange;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastLogin;

	@Email(message = "Invalid User Name !!")
	@NotBlank(message = "email required !!")
	private String email;

	private int failedAttempts;

	private int optlock;

	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate changePasswordDate;

	private int encryptionScheme;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime accountLockedTime;

	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate accountDisabledDate;

	private EntitysDto entitys;

	private LanguageDto language;

	private CurrencyDto currency;

	private UserStatusDto userStatus;
	
	private int roleId;    //role id to manage role only like 1 is Admin otherwise normal user
	
	private Set<RoleDto> roles = new HashSet<>();
}
