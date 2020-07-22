package com.roombooking.system.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.roombooking.system.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

	Optional<Customer> getCustomerByEmail(String email);
}
