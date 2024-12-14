package com.wpits.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wpits.entities.BaseUser;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.BaseUserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private BaseUserRepository baseUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		BaseUser user = baseUserRepository.findByEmail(username).orElseThrow( () -> new ResourceNotFoundException("base user not found with given email"));
		
		return user;
	}

}
