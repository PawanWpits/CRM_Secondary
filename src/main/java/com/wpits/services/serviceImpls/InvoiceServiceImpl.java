package com.wpits.services.serviceImpls;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wpits.dtos.InvoiceDto;
import com.wpits.entities.BillingProcess;
import com.wpits.entities.Currency;
import com.wpits.entities.Customer;
import com.wpits.entities.Invoice;
import com.wpits.entities.PaperInvoiceBatch;
import com.wpits.entities.Invoice.Status;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.BillingProcessRepository;
import com.wpits.repositories.CurrencyRepository;
import com.wpits.repositories.CustomerRepository;
import com.wpits.repositories.InvoiceRepository;
import com.wpits.repositories.PaperInvoiceBatchRepository;
import com.wpits.services.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService{

	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private BillingProcessRepository billingProcessRepository;
	@Autowired
	private PaperInvoiceBatchRepository paperInvoiceBatchRepository;
	@Autowired
	private CurrencyRepository currencyRepository;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private CustomerRepository customerRepository;
	
	private final Random random = new Random();
	
	@Override
	public InvoiceDto createInvoice(InvoiceDto invoiceDto, int billingProcessId, int paperInvoiceId,int currencyId) {
		BillingProcess billingProcess = billingProcessRepository.findById(billingProcessId).orElseThrow( () -> new ResourceNotFoundException("billing process not found with given id !!"));
		PaperInvoiceBatch paperInvoiceBatch = paperInvoiceBatchRepository.findById(paperInvoiceId).orElseThrow( () -> new ResourceNotFoundException("paper invoice batch not found with given id !!"));
		Currency currency = currencyRepository.findById(currencyId).orElseThrow( () -> new ResourceNotFoundException("currency not found with given id !!"));
		Invoice invoice = mapper.map(invoiceDto, Invoice.class);
		invoice.setBillingProcess(billingProcess);
		invoice.setPaperInvoiceBatch(paperInvoiceBatch);
		invoice.setCurrency(currency);
		invoice.setCreateDateTime(LocalDateTime.now());
		invoice.setDueDate(LocalDate.now());
		invoice.setPaymentAttempts(0);
//		invoice.setStatusId(1);
		invoice.setInProcessPayment(1);
		invoice.setDeleted(0);
		invoice.setLastReminder(LocalDate.now());
		invoice.setCreateTimeStamp(LocalDateTime.now());
		invoice.setOptlock(random.nextInt(999999));
		return mapper.map(invoiceRepository.save(invoice), InvoiceDto.class);
	}

	@Override
	public InvoiceDto updateInvoice(InvoiceDto invoiceDto, int invoiceId) {
		Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow( () -> new ResourceNotFoundException("invoice not found with given id !!!"));
		invoice.setCreateDateTime(LocalDateTime.now());
		invoice.setUserId(invoiceDto.getUserId());
		invoice.setDueDate(LocalDate.now());
		invoice.setTotal(invoiceDto.getTotal());
		invoice.setPaymentAttempts(invoiceDto.getPaymentAttempts());
		invoice.setStatus(invoiceDto.getStatus());
		invoice.setBalance(invoiceDto.getBalance());
		invoice.setCarriedBalance(invoiceDto.getCarriedBalance());
		invoice.setInProcessPayment(invoiceDto.getInProcessPayment());
		invoice.setIsReview(invoiceDto.getIsReview());
		invoice.setDeleted(invoiceDto.getDeleted());
		invoice.setCustomerNotes(invoiceDto.getCustomerNotes());
		invoice.setPublicNumber(invoiceDto.getPublicNumber());
		invoice.setLastReminder(LocalDate.now());
		invoice.setOverdueStep(invoiceDto.getOverdueStep());
		invoice.setCreateTimeStamp(LocalDateTime.now());
		invoice.setOptlock(invoiceDto.getOptlock());		
		return mapper.map(invoiceRepository.save(invoice), InvoiceDto.class);
	}

	@Override
	public List<InvoiceDto> getAllInvoice() {
		return invoiceRepository.findAll().stream().map(invoice -> mapper.map(invoice, InvoiceDto.class)).collect(Collectors.toList());
	}

	@Override
	public InvoiceDto findByIdInvoice(int invoiceId) {
		Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow( () -> new ResourceNotFoundException("invoice not found with given id !!"));		
		return mapper.map(invoice, InvoiceDto.class);
	}

	@Override
	public void deleteInvoice(int invoiceId) {
		Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow( () -> new ResourceNotFoundException("invoice not found with given id !!"));
		invoiceRepository.delete(invoice);		
	}
	
	//invoice CronJob by nextInvoceDate
	
	@Transactional
	public void genrateInvoice() {
		
		try {
			
			System.out.println("Invoice Cron job date time : " + LocalDateTime.now());
			
			LocalDate today = LocalDate.now();
			
			LocalDate yesterday = today.minusDays(1);
			System.out.println("customer's pick nextinvoice Date (yesterday) : " +yesterday);
			
			//nextInvoiceDate set by customer controller assigned plan time
			//ya to status Active ho Ya phir VIP ho tbhi chlega kyuki hm VIP ko block nhi kr rhe
			List<Customer> customers = customerRepository.findByNextInoviceDateAndCustomerTypeAndServiceStatusAndMonthlyLimitNotNull(yesterday,"postpaid",Customer.Status.ACTIVE);
			System.out.println("@@@@" + customers.size());
			
			for (Customer customer : customers) {
				
				System.out.println("invoice customers : " + customer.getId());
				
	            double invoiceAmount;
	            
	            if (customer.getCurrentMonthlyAmount() != null && customer.getCurrentMonthlyAmount() > customer.getMonthlyLimit()) {
	            	
	                invoiceAmount = customer.getCurrentMonthlyAmount();
	                
	            } else {
	            	
	                invoiceAmount = customer.getMonthlyLimit();
	            }
	            
	            //LocalDate nextInoviceDate = customer.getNextInoviceDate();
	            
				Invoice invoice = new Invoice();
				
				invoice.setCreateDateTime(LocalDateTime.now());
				invoice.setUserId(customer.getBaseUser().getId());
				invoice.setDueDate(customer.getNextInoviceDate().withDayOfMonth(customer.getNextInoviceDate().getMonth().length(customer.getNextInoviceDate().isLeapYear()))); //it's return last date of month 
				invoice.setPaymentAttempts(0);
				//invoice.setTotal(customer.getCurrentMonthlyAmount());
				invoice.setBillAmount(invoiceAmount);
				invoice.setLateFee(0.0);
				invoice.setTotal(invoiceAmount);
				invoice.setCustomerId(customer.getId());
				invoice.setStatus(Invoice.Status.UNPAID);
				invoice.setBalance(0.0);
				invoice.setCarriedBalance(0.0);
				invoice.setInProcessPayment(0);
				invoice.setIsReview(0);
				invoice.setDeleted(0);
				invoice.setCustomerNotes("Thank you for using Neotel Services");
				invoice.setPublicNumber(customer.getAlternateNumber());
				invoice.setMsisdn(customer.getSimInventory().getMsisdn());
				invoice.setLastReminder(today);
				invoice.setOverdueStep(0);
				invoice.setInvoiceNumber("NR"+random.nextInt(999999999) +"" +(char) ('A' + random.nextInt(26)));
				invoice.setBillStartDate(customer.getNextInoviceDate().minusMonths(1).plusDays(1));
				invoice.setBillEndDate(customer.getNextInoviceDate());
				invoice.setVip(customer.isVip());
				invoice.setOptlock(random.nextInt(999999));
				invoice.setCreateTimeStamp(LocalDateTime.now());
				invoice.setCurrency(customer.getAccountType().getCurrency());
				
				invoiceRepository.save(invoice);
				
				//nextInvoice Date increase 1 month  in customer
				customer.setNextInoviceDate(customer.getNextInoviceDate().plusMonths(1));
				
				customerRepository.save(customer);
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Map<String, Object> invoiceDetails() {
		
		Map<String, Object> result = new HashMap<>();

		/*	    List<Invoice> invoices = invoiceRepository.findAll();
		System.out.println("Total invoices: " + invoices.size());
		*/
	    //List<InvoiceDto> paidInvoices = invoices.stream().filter(invoice -> invoice.getStatus() == Invoice.Status.PAID).map(invoice -> mapper.map(invoice, InvoiceDto.class)).collect(Collectors.toList());

	    List<InvoiceDto> unpaidInvoices = invoiceRepository.findAll().stream().filter(invoice -> invoice.getStatus() == Invoice.Status.UNPAID).map(invoice -> mapper.map(invoice, InvoiceDto.class)).collect(Collectors.toList());

		/*	    Map<String, Integer> invoiceCount = new HashMap<>();
		invoiceCount.put("paidInvoiceCount", paidInvoices.size());
		invoiceCount.put("unpaidInvoiceCount", unpaidInvoices.size());
		
		result.put("invoiceCount", invoiceCount);*/
	    result.put("invoiceDetails", Map.of("unpaidInvoiceCount", unpaidInvoices.size(), "unpaidInvoices", unpaidInvoices));

	    return result;
	}

	@Override
	public List<InvoiceDto> findInvoiceByMsisdn(String msisdn) {

		return invoiceRepository.findByMsisdnOrderByIdDesc(msisdn).stream().map(invoice -> mapper.map(invoice, InvoiceDto.class)).collect(Collectors.toList());

	}

	@Override
	public InvoiceDto findUnpaidInviceByMsisdn(String msisdn) {
		Invoice invoice = invoiceRepository.findByMsisdnAndStatus(msisdn, Invoice.Status.UNPAID).orElseThrow( () -> new ResourceNotFoundException("invoice not found with given msisdn !!"));
		return mapper.map(invoice, InvoiceDto.class);
	}

}
