package dev.abreu.bankapp.dao;

import java.util.List;
import java.util.Optional;

import dev.abreu.bankapp.model.Customer;

/**
 * This interface provides the basic CRUD operations for
 * the Customer model
 * 
 * @author Devin Abreu
 *
 */
public interface CustomerDao {

	Optional<Customer> findByUsername(String username);
	
	Optional<Customer> findById(Long customerId);
	
	List<Customer> findAllCustomers();
	
	boolean existsByUsername(String username);
	
	Customer saveCustomer(Customer customer);
	
	Customer updateCustomer(Customer customer);
	
	boolean deleteCustomerByUsername(String username);
	
	boolean deleteCustomerById(Long customerId);

}
