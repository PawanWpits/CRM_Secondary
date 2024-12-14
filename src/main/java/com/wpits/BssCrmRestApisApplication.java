package com.wpits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableWebMvc
public class BssCrmRestApisApplication implements CommandLineRunner{
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BssCrmRestApisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println(passwordEncoder.encode("pawan123"));
	}

}
