package com.wpits.services.serviceImpls;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.wpits.dtos.ApiResponseMessage;
import com.wpits.dtos.CartDto;
import com.wpits.entities.Cart;
import com.wpits.entities.Customer;
import com.wpits.entities.DeviceInventory;
import com.wpits.entities.Partner;
import com.wpits.entities.RouterInventory;
import com.wpits.exceptions.ResourceNotFoundException;
import com.wpits.repositories.CartRepository;
import com.wpits.repositories.CustomerRepository;
import com.wpits.repositories.DeviceInventoryRepository;
import com.wpits.repositories.PartnerRepository;
import com.wpits.repositories.RouterInventoryRepository;
import com.wpits.services.CartService;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private DeviceInventoryRepository deviceInventoryRepository;
	
	@Autowired
	private RouterInventoryRepository routerInventoryRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ModelMapper mapper;

	public ApiResponseMessage createCart(String token, int deviceID, int partnerId, int routerSerialNo) {
		
	    Cart cart = new Cart();

		/*try {*/
	    	
	    List<Customer> customers = customerRepository.findByEkycToken(token);
	    if (customers.isEmpty()) {
	        throw new ResourceNotFoundException("customer not found with the given token!");
	    }

	    Customer customer = customers.get(0);
	    cart.setToken(customer.getEkycToken());

	    //device
	    if (deviceID > 0) {
	    	
	        DeviceInventory deviceInventory = deviceInventoryRepository.findById(deviceID)
	                .orElseThrow(() -> new ResourceNotFoundException("device not found with the given ID!"));

	        List<Cart> cartDevice = cartRepository.findByDeviceId(deviceID);
	        
	        if (cartDevice != null) {
	        	
	            for (Cart cart_ : cartDevice) {
	            	
	                if (cart_.getDeviceId() == deviceID && cart_.getToken().equals(token)) {
	                    throw new ResourceNotFoundException("this device is already present in the cart!");
	                }
	            }
	        }

	        if (partnerId > 0) {
	        	
	            Partner partner = partnerRepository.findById(partnerId)
	                    .orElseThrow(() -> new ResourceNotFoundException("partner not found with the given ID!"));
	            
	            cart.setPartnerId(partner.getId());

	            if (partnerId != deviceInventory.getPartnerId()) {
	                throw new ResourceNotFoundException("dear partner, this device does not belong to your inventory!");
	            }
	        }

	        if (deviceInventory.getAllocationDate() != null) {
	            throw new ResourceNotFoundException("this device has already been assigned!");
	        }

	        cart.setDeviceId(deviceInventory.getId());
	        cart.setAmount(deviceInventory.getSellingPriceUsd());
	        cart.setName(deviceInventory.getDeviceModel());
	        cart.setType(deviceInventory.getDeviceType());
	    }

	    // router
	    if (routerSerialNo > 0) {
	    	
	        RouterInventory routerInventory = routerInventoryRepository.findBySerialNumber(routerSerialNo)
	                .orElseThrow(() -> new ResourceNotFoundException("router not found with the given serial number!"));

	        List<Cart> cartRouter = cartRepository.findByRouterSerialNo(routerSerialNo);
	        
	        if (cartRouter != null) {
	        	
	            for (Cart cart_ : cartRouter) {
	            	
	                if (cart_.getRouterSerialNo() == routerSerialNo && cart_.getToken().equals(token)) {
	                    throw new ResourceNotFoundException("this router is already present in the cart!");
	                }
	            }
	        }

	        if (routerInventory.getAllocationDate() != null) {
	            throw new ResourceNotFoundException("this router has already been assigned!");
	        }

	        cart.setRouterSerialNo(routerInventory.getSerialNumber());
	        cart.setAmount(0.0);
	        cart.setName(routerInventory.getBrand());
	        cart.setType(routerInventory.getType());
	    }

	    cart.setCreateDate(LocalDate.now());
	    cart.setExpiryDate(LocalDate.now().plusDays(3)); // cat items expire after 3 days if not paid

	    
	    	
	        cartRepository.save(cart);
	        
	        return ApiResponseMessage.builder()
	                .message("Cart updated successfully!")
	                .success(true)
	                .status(HttpStatus.OK)
	                .build();
	   /* } catch (Exception e) {
	    	e.printStackTrace();
	        return ApiResponseMessage.builder()
	                .message("Failed to update cart!" +e.getMessage())
	                .success(false)
	                .status(HttpStatus.BAD_REQUEST)
	                .build();
	    }*/
	}


	@Override
	public List<CartDto> getCart(String token) {
		System.out.println(token);
		List<Cart> carts = cartRepository.findByToken(token);
		System.out.println(carts.size());
		if (carts.isEmpty()) {
			throw new ResourceNotFoundException("cart not found with given token!!");
		}
		

		return carts.stream().map( cart -> mapper.map(cart, CartDto.class)).collect(Collectors.toList());
		
		/*		for (Cart cart : cartIems) {
					
					
				}*/
		
	}

	@Override
	public void deleteCart(int cartId) {
		
		Cart cart = cartRepository.findById(cartId).orElseThrow( () -> new ResourceNotFoundException("cart not found with given id !!"));
		
		cartRepository.delete(cart);
		
	}

	@Override
	public void clearCartByExpireDate() {
		
		LocalDate today = LocalDate.now();
		System.out.println(today);
		
		List<Cart> expireCartItems = cartRepository.findByExpiryDate(today);
		
		cartRepository.deleteAll(expireCartItems);
		
	}

}
