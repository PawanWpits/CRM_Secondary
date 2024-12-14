package com.wpits.services;

import java.util.List;

import com.wpits.dtos.BaseUserDto;

public interface BaseUserService {

	BaseUserDto createBaseUser(BaseUserDto baseUserDto, int entityId,int languageId,int currencyId,int userStatusId);
	
	BaseUserDto updateBaseUser(BaseUserDto baseUserDto,int baseUserId);
	
	List<BaseUserDto> getAllBaseUser();
	
	void deleteBaseUser(int baseUserId);
}
