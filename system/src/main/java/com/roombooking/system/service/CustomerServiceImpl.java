package com.roombooking.system.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roombooking.system.model.Customer;
import com.roombooking.system.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepo;
	
	@Override
	public Optional<Customer> getCustomerById(long id) {
		return customerRepo.findById(id);
	}

	@Override
	public Customer addCustomer(Customer newCustomer) {
		return customerRepo.save(newCustomer);
	}

	@Override
	public Optional<Customer> getCustomerByEmail(String email) {
		return customerRepo.getCustomerByEmail(email);
	}

}
