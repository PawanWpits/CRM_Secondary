package com.wpits.helpers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.hibernate.dialect.MySQL8Dialect;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Testing {
	
	/* Developed by - P.K.Sharma */
	//public static void main(String[] args) {	

		//private LocalDate date;
		
		//String date=DateTimeFormatter.ofPattern("dd/MMM/YYYY").format(LocalDate.now());
		/*
		 * LocalDate localDate =
		 * LocalDate.parse(DateTimeFormatter.ofPattern("dd/MMM/YYYY").format(LocalDate.
		 * now())); System.out.println(localDate);
		 */
		
		
		
		//LocalDate now = LocalDate.now();
		
		//System.out.println("date :"+ LocalDate.now());
		
		//System.out.println(DateTimeFormatter.ofPattern("dd/MMMM/YYYY").format(LocalDateTime.now()));
		
		//System.out.println(DateTimeFormatter.ofPattern("dd/MMM/YYYY").format(LocalDate.now()));
		
		/*
		 * LocalDate date = LocalDate.now(); DateTimeFormatter formatters =
		 * DateTimeFormatter.ofPattern("dd/MMM/yyyy"); String text =
		 * date.format(formatters); LocalDate parsedDate = LocalDate.parse(text,
		 * formatters);
		 * 
		 * System.out.println("date: " + date); System.out.println("Text format " +
		 * text); System.out.println("parsedDate: " + parsedDate.format(formatters));
		 */
		    
		/*
		 * String format = parsedDate.format(formatters); LocalDate d1 =
		 * LocalDate.parse(format); System.out.println("######"+d1+"");
		 */
		
		    
		    
			/*
			 * LocalDate localDate =
			 * LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern(
			 * "d/MMM/uuuu"),)); System.out.println("@@@@@@@ localDate :->" +localDate +
			 * "@@@@@@@@@@@@");
			 */
		
		/*
		 * LocalDate dt = LocalDate.parse(date,
		 * DateTimeFormatter.ofPattern("dd/MMM/YYYY"));
		 * System.out.println("Default format after parsing = "+dt);
		 */
		
		/*
		 * 
		 * LocalDate currentDate = LocalDate.now();
		 * 
		 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
		 * 
		 * String formattedDateString = currentDate.format(formatter);
		 * 
		 * LocalDate formattedDate = LocalDate.parse(formattedDateString, formatter);
		 * 
		 * System.out.println(formattedDate);
		 */
		/*
		 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
		 * LocalDate currentDate = LocalDate.now(); LocalDate formattedDate =
		 * LocalDate.parse(currentDate.format(formatter), formatter);
		 * System.out.println("@@@@@@@@@@@@@@@@@"+formattedDate);
		 */
		
		
		//System.out.println(new SimpleDateFormat("dd/MMM/yyyy").format(new Date()));
		   // LocalDate currentDate = LocalDate.now();

	       // String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy"));

	       // System.out.println("Formatted Date: " + formattedDate);
	        
	        
	        
			/*
			 * LocalDate date = LocalDate.now(); DateTimeFormatter formatter =
			 * DateTimeFormatter.ofPattern("d MMM uuuu"); String text =
			 * date.format(formatter); LocalDate parsedDate = LocalDate.parse(text,
			 * formatter); System.out.println(parsedDate);
			 */
		
    
//	}
	
			/*
			 * public static void main(String[] args) {
			 * System.out.println(LocalDateTime.now()); String strDatewithTime =
			 * "2024-04-09T11:50:03.846882500"; LocalDateTime aLDT =
			 * LocalDateTime.parse(strDatewithTime); System.out.println("Date with Time: " +
			 * aLDT);
			 * 
			 * String ekycDate="2024-04-09 11:50:03"; System.out.println(
			 * LocalDateTime.parse(ekycDate,
			 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))); }
			 */
	
			/*
			 * public static void main(String[] args) {
			 * 
			 * Random r = new Random(); int num = r.nextInt(999999999); char c =
			 * (char)(r.nextInt(26) + 'A'); String orderId = num+"" +c+"";
			 * System.out.println(orderId); }
			 */
	
	
	
	
	
			/*
			 * static int value1=10;
			 * 
			 * static { value1=20; System.out.println(value1); }static { value1=30;
			 * System.out.println(value1); }
			 * 
			 * //static int value2=00;
			 * 
			 * public static void main(String[] args) { System.out.println(value1); }
			 * 
			 */
	 
//	 public static void main(String[] args) {
//		
//		 
//		 DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//	        String date1 = "01/05/2024";
//	        LocalDate today = LocalDate.now();
//	        
//
//	        LocalDate gracePeriodDate = LocalDate.parse(date1, format);
//
//	        long overdueDays = ChronoUnit.DAYS.between(gracePeriodDate, today);
//	        System.out.println("Days between: " + overdueDays);
//	        
//	        if (overdueDays > 10) {
//				System.out.println("status block ");
//			} else if(overdueDays > 17) {
//				System.out.println("status suspand ");
//			}
//	}
		
//			public static void main(String[] args) {
//				 char c=(char) ('A' + new Random().nextInt(26));
//				 String string = String.valueOf(System.currentTimeMillis());
//				 //System.out.println(c);
//				 //System.out.println(string); 
//				System.out.println(string + c);
//
//				System.out.println(c +string);
//
//			}
			
//			public static void main(String[] args) {
//				String str = "Activate";
//				
//				String lowerCase = str.toLowerCase();
//				System.out.println(lowerCase);
//				
//				if (lowerCase.equals("activate")) {
//					System.out.println("@@@@@@@@@@@@@@");
//				}
//			}
/*	 static Map<String,String> pizza = new HashMap<String, String>();
	 
	 public static void main(String[] args) {
		 
	  pizza.put(null, "basic pizza");
	  pizza.put("margharita", "mouth watering italian delicacy");
	  pizza.put("meditarriean", "overloaded with meditarian herbs");
	  pizza.put("New York", "super fatty, super tasty, super thin");
	  
	  
	  
	  Testing order = new Testing();
	  System.out.println( order.getPizza(null));
	  
	  
	 }


	 static String getPizza(String key) {
	  return pizza.get(key);
	 }*/
	
/*	}*/
			

	
/*	public static void main(String[] args) {
			
		try {
			
			System.out.println("************** UDM/HSS UN_PROVISIONING *******************************");
			
			String URL_UDM = "http://172.5.10.2:9697/api/hss/detail/block/subscriber/001010617991191";
			System.out.println("URL_UDM " + URL_UDM);
			
			RestTemplate rest_template = new RestTemplate();
			
			HttpHeaders http_headers = new HttpHeaders();
			http_headers.setContentType(MediaType.APPLICATION_JSON);
			
			HttpEntity<?> request_entity1 = new HttpEntity<>(http_headers);
			
			ResponseEntity<String> response_Entity = rest_template.exchange(URL_UDM, HttpMethod.PUT, request_entity1, String.class);
			
			String response_Body = response_Entity.getBody();
			System.out.println("UDM/HSS response : " + response_Body);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}*/
	
/*		public static void main(String[] args) {
			
//			Integer[] arr = {1,2,3,4};
//			List<Integer> list = Arrays.asList(arr);
//			
//			list.set(1, 1000);
//			//list.add(99);
//			System.out.println(list);
			
	        String[] array = new String[]{"one", "two", "three"};
	        List<String> list = List.of(array);
	        //array[0] = "thousand";
	        
	        list.set(1, "pawan");
	        
	        System.out.println(list);
		}*/	
	
	public static void main(String[] args) {
		Random r = new Random();
		int num = r.nextInt(999999999);
		char c = (char) (r.nextInt(26) + 'A');
		String orderId = num + "" + c + "";
		System.out.println(orderId);
		
		int next = ThreadLocalRandom.current().nextInt(999999999);

		System.out.println(next);
		
		String number = ThreadLocalRandom.current().nextInt(999999999)+"";
		System.out.println("String concat" + number);
		
		String n = String.valueOf(ThreadLocalRandom.current().nextInt(999999999));
		System.out.println(n);
	}
		}
