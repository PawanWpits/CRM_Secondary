package com.wpits.services;

import java.util.List;

import com.wpits.dtos.UserStatusDto;

public interface UserStatusService {

	UserStatusDto createUserStatus(UserStatusDto userStatusDto);
	UserStatusDto updateUserStatus(UserStatusDto userStatusDto,int userStatusId);
	List<UserStatusDto> getAllUserStatus();
	void deleteUserStatus(int userStatusId);
}
