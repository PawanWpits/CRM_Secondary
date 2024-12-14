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
import com.wpits.dtos.EntitysDto;
import com.wpits.services.EntitysService;

@RestController
@RequestMapping("/api")
public class EntitysController {

	@Autowired
	private EntitysService entitysService;
	
	@PostMapping("/saveentitys/currency/{currencyId}/language/{languageId}")
	public ResponseEntity<EntitysDto> saveEntitys(@RequestBody EntitysDto entitysDto,@PathVariable int currencyId,@PathVariable int languageId){
		
		EntitysDto entitys = entitysService.createEntitys(entitysDto, currencyId, languageId);
		return new ResponseEntity<EntitysDto>(entitys,HttpStatus.CREATED);
	}
	
	@PutMapping("/updateentitys/{entityId}")
	public ResponseEntity<EntitysDto> updateEntitys(@RequestBody EntitysDto entitysDto,@PathVariable int entityId){
		
		EntitysDto entitys = entitysService.updateEntitys(entitysDto, entityId);
		return ResponseEntity.ok(entitys);
	}
	
	@GetMapping("/entitys/{entityId}")
	public ResponseEntity<EntitysDto> singleEntitys(@PathVariable int entityId){
		
		//entitysService.getByIdEntitys(entityId);
		return ResponseEntity.ok(entitysService.getByIdEntitys(entityId));
	}
	
	@GetMapping("/allentitys")
	public ResponseEntity<List<EntitysDto>> getAllEntitys(){
		return ResponseEntity.ok(entitysService.getAllEntitys());
	}
	
	@DeleteMapping("/deleteentitys/{entityId}")
	public ResponseEntity<ApiResponseMessage> deleteEntitys(@PathVariable int entityId){
		
		entitysService.deleteEntitys(entityId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
	
}
