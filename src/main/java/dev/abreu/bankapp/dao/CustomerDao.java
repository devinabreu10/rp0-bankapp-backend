package dev.abreu.bankapp.dao;

import dev.abreu.bankapp.model.Customer;

/**
 * Along with using Spring Data JDBC I will also be using the JDBC API
 * for extra practice with both methods
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
	public Customer findByUsername(String username);
	
	public Customer saveCustomer(Customer customer);
	
	public Customer updateCustomer(Customer customer);
	
	public Customer deleteCustomer(Customer customer);
	
}
