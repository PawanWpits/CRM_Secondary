package com.wpits.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.BaseUserDto;
import com.wpits.dtos.JwtRequest;
import com.wpits.dtos.JwtResponse;
import com.wpits.exceptions.BadApiRequestException;
import com.wpits.security.JwtHelper;
import com.wpits.services.BaseUserService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api")
@Api(value = "AuthController", description = "APIs for Authentication !!")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private BaseUserService userService;

    @Autowired
    private JwtHelper helper;
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
		this.doAuthenticate(request.getEmail(), request.getPassword());
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		String token = this.helper.generateToken(userDetails);
		BaseUserDto userDto = modelMapper.map(userDetails, BaseUserDto.class);
		JwtResponse response = JwtResponse.builder().jwtToken(token).user(userDto).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void doAuthenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		try {
			manager.authenticate(authentication);
		} catch (BadCredentialsException e) {
			throw new BadApiRequestException(" Invalid Username or Password  !!");
		}

	}
}
