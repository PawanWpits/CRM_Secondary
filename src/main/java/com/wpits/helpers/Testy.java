package com.wpits.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Testy {
	
	Logger logger = LoggerFactory.getLogger(Testy.class);

	public static void main(String[] args) {

		//new Testy().simProvision("123456789123456", "6741233211");
		
		//new Testy().createProfileInPCF("123456789123456");
	}

	private void simProvision(String imsi, String msisdn) {

		try {

			System.out.println("************** UDM/HSS PROVISIONING *******************************");
			// String loginUrl = "";
			String provisionUrl = "http://172.17.1.11:9697/api/hss/detail/save/subscriber";

			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer e84e25f9-1613-4e55-957e-7a683ad3bb54");

			Random r = new Random();
			int num = r.nextInt(999999999);
			char c = (char) (r.nextInt(26) + 'A');
			String orderId = num + "" + c + "";
			// System.out.println(orderId);

			String requestBody = "{ \"orderId\": \"" + orderId + "\", \"imsi\": \""
					+ imsi + "\", \"msisdn\": \""
					+ msisdn + "\","
					+ " \"serviceCapability\": { \"Attach\": { \"LTE\": true, \"NR\": true, \"IMS\": true },"
					+ " \"Voice\": { \"Outgoing\": true, \"Incoming\": true },"
					+ " \"SMS\": { \"OutgoingSms\": true, \"OutgoingServiceSms\": true, \"IncomingSms\": true },"
					+ " \"DataService\": { \"LTE\": true, \"NR\": true } } }";
			System.out.println("@@@@@@@@" + requestBody);
			logger.info("request {}",requestBody);

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(provisionUrl, request, String.class);
			
			logger.info("UDM / HSS Provisioning :{} ",response);

				// activation date set in sim inventory when provisioning status 200 ok
				//simInventoryRepository.updateActivationDate(LocalDateTime.now(),customer.getSimInventory().getMsisdn());
		} catch (Exception e) {
			logger.error("error from HSS :{}", e.getMessage());
		}
	}
	
	private void createProfileInPCF(String imsi) {
		
		try {
			System.out.println("################## PCF/UDR Service ###########################");
			
			String URL = "http://172.17.1.22:18780/api/udr/subscriber/create-profile";
			
			System.out.println("@@@@@@@@@@@@@" + URL);
			
			RestTemplate template = new RestTemplate();
		
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
			Map<String, String> udrRequest = new HashMap<>();
			udrRequest.put("supi", imsi);
			udrRequest.put("subscriberProfile", "Redirect");
		
			HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(udrRequest,httpHeaders);
		
			ResponseEntity<String> response = template.exchange(URL, HttpMethod.POST,requestEntity, String.class);
			
			logger.info("PCF/UDR :{} ",response);
		}catch(Exception e) {
			logger.error("error from PCF/UDR :{}", e.getMessage());
		}
	}
}
