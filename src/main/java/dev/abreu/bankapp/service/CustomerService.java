package dev.abreu.bankapp.service;

import java.util.List;
import dev.abreu.bankapp.exception.UsernameTakenException;
import dev.abreu.bankapp.model.Customer;

public interface CustomerService {
	
	
	/**
	 * Register a new customer, independent of Account
	 * 
	 * @param customer
	 * @return Customer
	 * @throws UsernameTakenException if username is already taken
	 */
	Customer registerNewCustomer(Customer customer) throws UsernameTakenException;

	/**
	 * Retrieve existing Customer based on username
	 * 
	 * @param username
	 * @return Customer
	 * @throws ResourceNotFoundException if Customer with username is not found
	 */
	Customer getCustomerByUsername(String username);
	
	/**
	 * Retrieve existing Customer based on customerId
	 * 
	 * @param customerId
	 * @return Customer
	 * @throws ResourceNotFoundException if Customer with id is not found
	 */
	Customer getCustomerById(Long customerId);
	
	
	/**
	 * Retrieve a list of all customers present in database
	 * 
	 * @return list of customers
	 */
	List<Customer> getAllCustomers();
	
	/**
	 * update existing Customer details
	 * 
	 * @param customer
	 * @return Customer
	 */
	Customer updateCustomerDetails(Customer customer);
	
	/**
	 * remove Customer based on username
	 * 
	 * @param customerId
	 * @return boolean
	 */
	boolean deleteCustomerByUsername(String username);
	
	/**
	 * remove Customer based on id
	 * 
	 * @param customerId
	 * @return boolean
	 */
	boolean deleteCustomerById(Long customerId);
}
