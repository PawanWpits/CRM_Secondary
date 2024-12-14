package com.wpits.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.LanguageDto;
import com.wpits.services.LanguageService;

@RestController
@RequestMapping("/api")
public class LanguageController {

	@Autowired
	private LanguageService languageService;
	
	@PostMapping("/savelanguage")
	public ResponseEntity<LanguageDto> saveLanguage(@RequestBody LanguageDto languageDto){
		
		LanguageDto language = languageService.createLanguage(languageDto);
		return new ResponseEntity<LanguageDto>(language,HttpStatus.CREATED);
	}

	@PutMapping("/updatelanguage/{languageId}")
	public ResponseEntity<LanguageDto> updateLanguage(@RequestBody LanguageDto languageDto, @PathVariable int languageId){
		
		LanguageDto language = languageService.updateLanguage(languageDto, languageId);
		return ResponseEntity.ok(language);
	}
	
	@GetMapping("/alllanguage")
	public ResponseEntity<List<LanguageDto>> getLanguages(){
		
		List<LanguageDto> language = languageService.getAllLanguage();
		return ResponseEntity.ok(language);
	}
	
	@DeleteMapping("/deletelanguage/{languageId}")
	public ResponseEntity<ApiResponseMessage> deleteLanguage(@PathVariable int languageId){
		
		languageService.deleteLanguage(languageId);
		ApiResponseMessage message = ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build();
		return ResponseEntity.ok(message);
	}
	
	
	
}
