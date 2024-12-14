package com.wpits.services.serviceImpls;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.AccountTypeDto;
import com.wpits.entities.AccountType;
import com.wpits.entities.Currency;
import com.wpits.entities.Entitys;
import com.wpits.entities.InvoiceDeliveryMethod;
import com.wpits.entities.Language;
import com.wpits.entities.OrderPeriod;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.AccountTypeRepository;
import com.wpits.repositories.CurrencyRepository;
import com.wpits.repositories.EntitysRepository;
import com.wpits.repositories.InvoiceDeliveryMethodRepository;
import com.wpits.repositories.LanguageRepository;
import com.wpits.repositories.OrderPeriodRepository;
import com.wpits.services.AccountTypeService;

@Service
public class AccountTypeServiceImpl implements AccountTypeService{
	
	@Autowired
	private AccountTypeRepository accountTypeRepository;
	@Autowired
	private CurrencyRepository currencyRepository;
	@Autowired
	private EntitysRepository entitysRepository;
	@Autowired
	private LanguageRepository languageRepository;
	@Autowired
	private OrderPeriodRepository orderPeriodRepository;
	@Autowired
	private InvoiceDeliveryMethodRepository invoiceDeliveryMethodDtoRepository;
	@Autowired
	private ModelMapper mapper;

	@Override
	public AccountTypeDto createAccountType(AccountTypeDto accountTypeDto, int currencyId, int entityId, int languageId, int orderperiodId, int invoiceDeliveryMethodId) {
		
		Currency currency = currencyRepository.findById(currencyId).orElseThrow( () -> new ResourceNotFoundException("currency not found with given id !!"));
		Entitys entitys = entitysRepository.findById(entityId).orElseThrow( () -> new ResourceNotFoundException("entity not found with given id !!"));
		Language language = languageRepository.findById(languageId).orElseThrow( () -> new ResourceNotFoundException("language not found with given id !!"));
		OrderPeriod orderPeriod = orderPeriodRepository.findById(orderperiodId).orElseThrow( () -> new ResourceNotFoundException("order period not found with given id !!"));
		InvoiceDeliveryMethod invoiceDeliveryMethod = invoiceDeliveryMethodDtoRepository.findById(invoiceDeliveryMethodId).orElseThrow( () -> new ResourceNotFoundException("invoice delevery method not found with given id !!"));
		
		AccountType accountType = mapper.map(accountTypeDto, AccountType.class);
		accountType.setCurrency(currency);
		accountType.setEntitys(entitys);
		accountType.setLanguage(language);
		accountType.setOrderPeriod(orderPeriod);
		accountType.setInvoiceDeliveryMethod(invoiceDeliveryMethod);
		accountType.setDateCreated(LocalDateTime.now());
		accountType.setOptlock(new Random().nextInt(999999));
		
		AccountType savedAccountType = accountTypeRepository.save(accountType);
		
		return mapper.map(savedAccountType, AccountTypeDto.class);
	}

	@Override
	public AccountTypeDto updateAccountType(AccountTypeDto accountTypeDto, int accountTypeId) {
		
		AccountType accountType = accountTypeRepository.findById(accountTypeId).orElseThrow( () -> new ResourceNotFoundException("account type not found with given id !!"));
		accountType.setCreditLimit(accountTypeDto.getCreditLimit());
		accountType.setInvoiceDesign(accountTypeDto.getInvoiceDesign());
		accountType.setDateCreated(LocalDateTime.now());
		accountType.setCreditNotificationLimit1(accountTypeDto.getCreditNotificationLimit1());
		accountType.setCreditNotificationLimit2(accountTypeDto.getCreditNotificationLimit2());
		accountType.setOptlock(accountTypeDto.getOptlock());
		accountType.setNextInvoiceDayOfPeriod(accountTypeDto.getNextInvoiceDayOfPeriod());
		accountType.setNotificationAitId(accountTypeDto.getNotificationAitId());
		AccountType savedAccountType = accountTypeRepository.save(accountType);
		return mapper.map(savedAccountType, AccountTypeDto.class);
	}

	@Override
	public List<AccountTypeDto> getAllAccountType() {
		
		return accountTypeRepository.findAll().stream().map( account -> mapper.map(account, AccountTypeDto.class)).collect(Collectors.toList());
	}

	@Override
	public void deleteAccountType(int accountTypeId) {
		
		AccountType accountType = accountTypeRepository.findById(accountTypeId).orElseThrow( () -> new ResourceNotFoundException("account type not found with given id !!"));
		accountTypeRepository.delete(accountType);	
	}

}
