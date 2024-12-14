package com.wpits.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wpits.entities.Customer;
import com.wpits.entities.Customer.Status;
import com.wpits.entities.Partner;

public interface CustomerRepository  extends JpaRepository<Customer, Integer> {

	@Modifying
	@Transactional
	@org.springframework.transaction.annotation.Transactional
	@Query(value = "update customer set payment_status=?1 where id=?2",nativeQuery = true)
	int updateCustomerPaymentStatus(Boolean paymentStatus,int cutomerId);


	@Query(value = "select * from customer where msisdn=?1",nativeQuery = true)
	Optional<Customer> findByMsisdn(String msisdn);
	
	/*
	 * @Query(value =
	 * "SELECT * FROM wpbilling.customer where partner_id=?1",nativeQuery = true)
	 * Optional<Customer> findByPatnerId(int partnerId);
	 */
	
	Optional<List<Customer>> findByPartner(Partner partner);
	
	
//	 List<Customer> findByNextInvoiceDateBeforeEqualAndCustomerType(LocalDate date, String customerType);

//	List<Customer> findByNextInoviceDateBeforeAndCustomerType(LocalDate date, String customerType);


	//List<Customer> findByNextInoviceDateBeforeAndCustomerTypeAndServiceStatusAndMonthlyLimitNotNull(LocalDate today, String string,Status active);
	
	List<Customer> findByNextInoviceDateAndCustomerTypeAndServiceStatusAndMonthlyLimitNotNull(LocalDate yesterday, String customerType,Status active);

	List<Customer> findByFirstNameContaining(String keyword);
	
	@Query(value = "SELECT * FROM customer c " + "LEFT JOIN sim_inventory s ON c.msisdn = s.msisdn "
			+ "WHERE (:keyword IS NULL OR c.first_name LIKE %:keyword% OR c.last_name LIKE %:keyword% OR "
			+ "c.ekyc_token LIKE %:keyword% OR c.electricity_meter_id LIKE %:keyword% OR "
			+ "CAST(c.service_status AS CHAR) LIKE %:keyword% OR CAST(c.monthly_limit AS CHAR) LIKE %:keyword% OR "
			+ "s.msisdn LIKE %:keyword%)", nativeQuery = true)
	List<Customer> searchCustomers(@Param("keyword") String keyword);


	List<Customer> findByEkycToken(String token);


	List<Customer> findByCustomerTypeAndServiceStatusNot(String customerType, Status active);


	List<Customer> findByEmail(String email);


	List<Customer> findByParentId(int parentCustomerId);


	@Query(value = "select * from customer where router_id=?1",nativeQuery = true)
	Optional<Customer> findByRouterId(String routerId);
}
