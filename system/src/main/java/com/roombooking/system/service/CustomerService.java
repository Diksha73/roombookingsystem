package com.roombooking.system.service;

import java.util.Optional;

import com.roombooking.system.model.Customer;

public interface CustomerService {

	Optional<Customer> getCustomerById(long id);
	
	Customer addCustomer(Customer newCustomer);
	
	Optional<Customer> getCustomerByEmail(String email);
	
}
