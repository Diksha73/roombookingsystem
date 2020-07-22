package com.roombooking.system.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roombooking.system.model.Customer;
import com.roombooking.system.service.CustomerService;

@RestController
@RequestMapping(value="/api")
public class AppController {

	@Autowired
	private CustomerService customerService;
	
	@PostMapping(value="/customer")
	public ResponseEntity addCustomer(@RequestBody Customer newCustomer) {
		
		if (null == newCustomer) {
			return ResponseEntity.badRequest().body(null);
		}
		Map<String, Object> result = new HashMap<>();
		
			if (null != newCustomer.getPassword() && newCustomer.getPassword().length() < 8 || newCustomer.getPassword().length() > 10) {
				 result.put("error", "password length should be between 8 to 10");
				return ResponseEntity.badRequest().body(result);
			}
			if (null != newCustomer.getEmail()
					&& customerService.getCustomerByEmail(newCustomer.getEmail()).isPresent()) {
				 result.put("error","User email already exist");
				 return ResponseEntity.badRequest().body(result);
			}
		URI uri = null;
		try {
			uri = new URI(newCustomer.toString());
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body(null);
		}
		Customer savedCustomer = customerService.addCustomer(newCustomer);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
	}
	
	@GetMapping(value="/customer/{id}")
	public ResponseEntity getCustomer(@PathVariable Long id) {
		Map<String, Object> result = new HashMap<>();
		Optional<Customer> customer = customerService.getCustomerById(id);
		if (!customer.isPresent()) {
			result.put("error", "User not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		}
		System.out.println("get method status : " + HttpStatus.OK);
		return ResponseEntity.ok(customer.get());
	}
	
}
