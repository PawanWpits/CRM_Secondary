package com.wpits.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wpits.dtos.ApiResponseMessage;

@RestControllerAdvice
public class GlobalExceptonHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMessage> resourceNotFoundException(ResourceNotFoundException ex){
		
		ApiResponseMessage response = ApiResponseMessage.builder()
				.message(ex.getMessage())
				.success(true)
				.status(HttpStatus.NOT_FOUND).build();
		
		return new ResponseEntity<ApiResponseMessage>(response, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		
		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		allErrors.stream().forEach(ObjectError -> {
			
			String message = ObjectError.getDefaultMessage();
			String field = ((FieldError)ObjectError).getField();
			response.put(field, message);
		});
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
	}
	
	
	
	@ExceptionHandler(DataIntegrityViolationException.class) public
	  ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
	  
	  Map<String, String> response= new HashMap<String, String>();
	  /*String message = ex.getRootCause().getMessage();*/
	  response.put("Database_error", ex.getRootCause().getMessage());
	  response.put("error_code", "409");
	  return new ResponseEntity<Map<String,String>>(response,HttpStatus.CONFLICT);
	  
	  }
	
	//handle bad api exception
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> handleBadApiRequest(BadApiRequestException ex) {
        //logger.info("Bad api request");
        ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST).success(false).build();
        return new ResponseEntity<ApiResponseMessage>(response, HttpStatus.BAD_REQUEST);

    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidInput(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Invalid request payload.");
    }
}
