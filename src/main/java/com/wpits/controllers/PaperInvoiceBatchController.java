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
import com.wpits.dtos.PaperInvoiceBatchDto;
import com.wpits.services.PaperInvoiceBatchService;

@RestController
@RequestMapping("/api")
public class PaperInvoiceBatchController {

	@Autowired
	private PaperInvoiceBatchService paperInvoiceBatchService;
	
	@PostMapping("/savepaperinvoice")
	public ResponseEntity<PaperInvoiceBatchDto> savePaperInvoiceBatch(@RequestBody PaperInvoiceBatchDto paperInvoiceBatchDto){
		return new ResponseEntity<PaperInvoiceBatchDto>(paperInvoiceBatchService.createpaperInvoiceBatch(paperInvoiceBatchDto),HttpStatus.CREATED);
	}
	
	@PutMapping("/updatepaperinvoice/{paperInvoceId}")
	public ResponseEntity<PaperInvoiceBatchDto> updatePaperInvoiceBatch(@RequestBody PaperInvoiceBatchDto paperInvoiceBatchDto,@PathVariable int paperInvoceId){
		return ResponseEntity.ok(paperInvoiceBatchService.updatepaperInvoiceBatch(paperInvoiceBatchDto, paperInvoceId));
	}
	
	@GetMapping("/paperinvoices")
	public ResponseEntity<List<PaperInvoiceBatchDto>> getAllPaperInvoiceBatchs(){
		return ResponseEntity.ok(paperInvoiceBatchService.getAllPaperInvoiceBatch());
	}
	
	@DeleteMapping("/deletepaperinvoice/{paperInvoceId}")
	public ResponseEntity<ApiResponseMessage> deletePaperInvoiceBatch(@PathVariable int paperInvoceId){
		paperInvoiceBatchService.deletePaperInvoiceBatch(paperInvoceId);
		return ResponseEntity.ok(ApiResponseMessage.builder().message("deleted successfully !!").success(true).status(HttpStatus.OK).build());
	}
	
}
