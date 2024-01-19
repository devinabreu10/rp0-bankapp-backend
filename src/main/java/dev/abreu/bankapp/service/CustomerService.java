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
	 * @throws UsernameTakenException 
	 */
	Customer registerNewCustomer(Customer customer) throws UsernameTakenException;
	
	//List<Account> getAllAccounts(Customer customer);
	
	
	/**
	 * Retrieve existing Customer based on username
	 * 
	 * @param username
	 * @return Customer
	 */
	Customer getCustomerByUsername(String username);
	
	
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
	 * remove Customer based on id
	 * 
	 * @param customerId
	 */
	void deleteCustomer(long customerId);
	
	
	/**
	 * Allow existing Customer to sign into online account
	 * 
	 * @param username
	 * @param password
	 * @return Customer
	 */
	Customer customerLogin(String username, String password);
	

}
