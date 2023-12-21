package dev.abreu.bankapp.dao;

import org.springframework.stereotype.Repository;

import dev.abreu.bankapp.model.Customer;

/**
 * Along with using Spring Data JDBC I will also be using the JDBC API
 * for extra practice with both methods
 * 
 * @author Devin Abreu
 *
 */
@Repository
public interface CustomerDao {
	
	/**
	 * Retrieves customer associated with provided username if valid
	 * 
	 * @param username being searched
	 * @return Customer associated with username
	 */
	Customer findByUsername(String username);
	
	void saveCustomer(Customer customer);
	
	void updateCustomer(Customer customer);
	
	void deleteCustomer(Long customerId);

}
