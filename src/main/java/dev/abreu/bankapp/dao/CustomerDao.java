package dev.abreu.bankapp.dao;

import java.util.List;

import dev.abreu.bankapp.model.Customer;

/**
 * This interface provides the basic CRUD operations for
 * the Customer model
 * 
 * @author Devin Abreu
 *
 */
public interface CustomerDao {
	
	/**
	 * Retrieves customer associated with provided username if valid
	 * 
	 * @param username being searched
	 * @return Customer associated with username
	 */
	Customer findByUsername(String username);
	
	Customer findById(Long customerId);
	
	List<Customer> findAllCustomers();
	
	boolean existsByUsername(String username);
	
	Customer saveCustomer(Customer customer);
	
	Customer updateCustomer(Customer customer);
	
	boolean deleteCustomerByUsername(String username);
	
	boolean deleteCustomerById(Long customerId);

}
