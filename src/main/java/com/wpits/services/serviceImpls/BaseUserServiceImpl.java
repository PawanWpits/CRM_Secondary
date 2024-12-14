package com.wpits.services.serviceImpls;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.wpits.dtos.BaseUserDto;
import com.wpits.entities.BaseUser;
import com.wpits.entities.Currency;
import com.wpits.entities.Entitys;
import com.wpits.entities.Language;
import com.wpits.entities.Role;
import com.wpits.entities.UserStatus;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.BaseUserRepository;
import com.wpits.repositories.CurrencyRepository;
import com.wpits.repositories.EntitysRepository;
import com.wpits.repositories.LanguageRepository;
import com.wpits.repositories.RoleRepository;
import com.wpits.repositories.UserStatusRepository;
import com.wpits.services.BaseUserService;

@Service
public class BaseUserServiceImpl implements BaseUserService{

	@Autowired
	private BaseUserRepository baseUserRepository;
	@Autowired
	private EntitysRepository entitysRepository;
	@Autowired
	private LanguageRepository languageRepository;
	@Autowired
	private CurrencyRepository currencyRepository;
	@Autowired
	private UserStatusRepository userStatusRepository;
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;
	
	private final Random random = new Random();
	
	@Override
	public BaseUserDto createBaseUser(BaseUserDto baseUserDto,int entityId,int languageId,int currencyId,int userStatusId) {
		
		Entitys entitys = entitysRepository.findById(entityId).orElseThrow( () -> new ResourceNotFoundException("entity not found with given id !!"));
		Language language = languageRepository.findById(languageId).orElseThrow( () -> new ResourceNotFoundException("language not found with given id !!"));
		Currency currency = currencyRepository.findById(currencyId).orElseThrow( () -> new ResourceNotFoundException("currency not found with given id !!"));
		UserStatus userStatus = userStatusRepository.findById(userStatusId).orElseThrow( () -> new ResourceAccessException("user status not found with given id !!"));
		Role role = roleRepository.findById(baseUserDto.getRoleId()).orElseThrow( () -> new ResourceNotFoundException("role not found with given id !!"));
		
		
		/*int roleId = baseUserDto.getRoleId();*/
		baseUserDto.setPassword(passwordEncoder.encode(baseUserDto.getPassword()));

		BaseUser baseUser = mapper.map(baseUserDto, BaseUser.class);
		
		/*Role role =null;
		if (roleId == 1) {
			role = roleRepository.findById(roleId).orElseThrow( () -> new ResourceNotFoundException("role not found with given id !!"));
		} else {
			role = roleRepository.findById(roleId).orElseThrow( () -> new ResourceNotFoundException("role not found with given id !!"));
		}*/
		System.out.println(role.getName());
		baseUser.getRoles().add(role);
		
		baseUser.setEntitys(entitys);
		baseUser.setLanguage(language);
		baseUser.setCurrency(currency);
		baseUser.setUserStatus(userStatus);
		baseUser.setDeleted(0); 
		baseUser.setSubscriberStatus(1);
		baseUser.setCreateDateTime(LocalDateTime.now());
		baseUser.setLastStatusChange(LocalDateTime.now());
		baseUser.setLastLogin(LocalDateTime.now());
		baseUser.setFailedAttempts(0);
		baseUser.setOptlock(random.nextInt(999999));
		baseUser.setChangePasswordDate(LocalDate.now());
		baseUser.setAccountLockedTime(LocalDateTime.now());
		baseUser.setAccountDisabledDate(LocalDate.now());
		BaseUser savedBaseUser = baseUserRepository.save(baseUser);
		return mapper.map(savedBaseUser, BaseUserDto.class);
	}

	@Override
	public BaseUserDto updateBaseUser(BaseUserDto baseUserDto, int baseUserId) {
		
		BaseUser baseUser = baseUserRepository.findById(baseUserId).orElseThrow( () -> new ResourceNotFoundException("base user not found with given id !!"));
		baseUser.setPassword(passwordEncoder.encode(baseUserDto.getPassword()));
		baseUser.setDeleted(0); 
		baseUser.setSubscriberStatus(1);
		baseUser.setCreateDateTime(LocalDateTime.now());
		baseUser.setLastStatusChange(LocalDateTime.now());
		baseUser.setLastLogin(LocalDateTime.now());
		baseUser.setEmail(baseUserDto.getEmail());
		baseUser.setFailedAttempts(0);
		baseUser.setOptlock(baseUserDto.getOptlock());
		baseUser.setChangePasswordDate(LocalDate.now());
		baseUser.setEncryptionScheme(baseUserDto.getEncryptionScheme());
		baseUser.setAccountLockedTime(LocalDateTime.now());
		baseUser.setAccountDisabledDate(LocalDate.now());
		BaseUser savedBaseUser = baseUserRepository.save(baseUser);
		return mapper.map(savedBaseUser, BaseUserDto.class);
	}

	@Override
	public List<BaseUserDto> getAllBaseUser() {
		return baseUserRepository.findAll().stream().map(baseUser -> mapper.map(baseUser, BaseUserDto.class)).collect(Collectors.toList());
	}

	@Override
	public void deleteBaseUser(int baseUserId) {
		BaseUser baseUser = baseUserRepository.findById(baseUserId).orElseThrow( () -> new ResourceNotFoundException("base user not found with given id !!"));
		baseUserRepository.delete(baseUser);
		
	}

}
