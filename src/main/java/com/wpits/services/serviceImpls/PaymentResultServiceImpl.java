package com.wpits.services.serviceImpls;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wpits.dtos.PaymentResultDto;
import com.wpits.entities.PaymentResult;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.PaymentResultRepository;
import com.wpits.services.PaymentResultService;

@Service
public class PaymentResultServiceImpl implements PaymentResultService{
	
	@Autowired
	private PaymentResultRepository paymentResultRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public PaymentResultDto createPaymentResult(PaymentResultDto paymentResultDto) {
		// TODO Auto-generated method stub
		return mapper.map(paymentResultRepository.save(mapper.map(paymentResultDto, PaymentResult.class)), PaymentResultDto.class);
	}

	@Override
	public List<PaymentResultDto> getAllPaymentResult() {
		return paymentResultRepository.findAll().stream().map(paymentResult -> mapper.map(paymentResult, PaymentResultDto.class)).collect(Collectors.toList());
	}

	@Override
	public void deletePaymentResult(int paymentResultId) {
		PaymentResult paymentResult = paymentResultRepository.findById(paymentResultId).orElseThrow( () -> new ResourceNotFoundException("payment Result not found with given id !!"));
		paymentResultRepository.delete(paymentResult);
	}

}
