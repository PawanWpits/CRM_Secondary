package com.wpits.services;

import java.util.List;

import com.wpits.dtos.AccountTypeDto;

public interface AccountTypeService {

	AccountTypeDto createAccountType(AccountTypeDto accountTypeDto,int currencyId,int entityId,int languageId,int orderperiodId,int invoiceDeliveryMethodId);
	
	AccountTypeDto updateAccountType(AccountTypeDto accountTypeDto,int accountTypeId);
	
	List<AccountTypeDto> getAllAccountType();
	
	void deleteAccountType(int accountTypeId);
}
