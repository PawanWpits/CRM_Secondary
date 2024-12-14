package com.wpits.services.serviceImpls;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.UserStatusDto;
import com.wpits.entities.UserStatus;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.UserStatusRepository;
import com.wpits.services.UserStatusService;

@Service
public class UserStatusServiceImpl implements UserStatusService{

	@Autowired
	private UserStatusRepository userStatusRepository;
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public UserStatusDto createUserStatus(UserStatusDto userStatusDto) {
		UserStatus userStatus = mapper.map(userStatusDto, UserStatus.class);
		UserStatus savedUserStatus = userStatusRepository.save(userStatus);
		return mapper.map(savedUserStatus, UserStatusDto.class);
	}

	@Override
	public UserStatusDto updateUserStatus(UserStatusDto userStatusDto, int userStatusId) {
		
		UserStatus userStatus = userStatusRepository.findById(userStatusId).orElseThrow( () -> new ResourceNotFoundException("user status not found with given id !!"));
		userStatus.setCanLogin(userStatusDto.getCanLogin());
		UserStatus savedUserStatus = userStatusRepository.save(userStatus);
		return mapper.map(savedUserStatus, UserStatusDto.class);
	}

	@Override
	public List<UserStatusDto> getAllUserStatus() {
			
		return userStatusRepository.findAll().stream().map(user -> mapper.map(user, UserStatusDto.class)).collect(Collectors.toList());
	}

	@Override
	public void deleteUserStatus(int userStatusId) {
		UserStatus userStatus = userStatusRepository.findById(userStatusId).orElseThrow( () -> new ResourceNotFoundException("user status not found with given id !!"));
		userStatusRepository.delete(userStatus);	
	}

}
