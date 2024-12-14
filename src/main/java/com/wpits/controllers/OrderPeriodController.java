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
import com.wpits.dtos.OrderPeriodDto;
import com.wpits.repositories.OrderPeriodRepository;
import com.wpits.services.OrderPeriodService;

@RestController
@RequestMapping("/api")
public class OrderPeriodController {

	@Autowired
	private OrderPeriodService orderPeriodService;
	
	@PostMapping("/saveorderperiod/entity/{entityId}/periodunit/{periodUnitId}")
	public ResponseEntity<OrderPeriodDto> saveOrderPeriod(@RequestBody OrderPeriodDto orderPeriodDto, @PathVariable int entityId, @PathVariable int periodUnitId){
		
		OrderPeriodDto orderPeriod = orderPeriodService.createOrderPeriod(orderPeriodDto, entityId, periodUnitId);
		return new ResponseEntity<OrderPeriodDto>(orderPeriod,HttpStatus.CREATED);
	}
	
	@PutMapping("/updateorderperiod/{orederPeriodId}")
	public ResponseEntity<OrderPeriodDto> updateOrderPeriod(@RequestBody OrderPeriodDto orderPeriodDto, @PathVariable int orederPeriodId){
		
		return ResponseEntity.ok(orderPeriodService.updateOrderPeriod(orderPeriodDto, orederPeriodId));
	}
	
	@GetMapping("/orderperiods")
	public ResponseEntity<List<OrderPeriodDto>> getAllOrderPeriod(){
		
		return ResponseEntity.ok(orderPeriodService.getAllOrderPeriod());
	}
	
	@DeleteMapping("/deleteorderperiod/{orederPeriodId}")
	public ResponseEntity<ApiResponseMessage> deleteOrderPeriod(@PathVariable int orederPeriodId){
		
		orderPeriodService.deleteOrderPeriod(orederPeriodId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
}
