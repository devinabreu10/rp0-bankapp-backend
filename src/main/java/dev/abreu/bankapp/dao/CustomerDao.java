package dev.abreu.bankapp.dao;

import java.util.List;
import java.util.Optional;

import dev.abreu.bankapp.model.Customer;

/**
 * The CustomerDao interface provides methods for interacting with the Customer
 * model in the database. It is responsible for finding, saving, updating, 
 * and deleting Customer objects.
 * 
 * @author Devin Abreu
 *
 */
public interface CustomerDao {

	/**
	 * Finds a Customer by username.
	 *
	 * @param username the username to search for
	 * @return an Optional containing the Customer if found, or an empty Optional if not found
	 */
	Optional<Customer> findByUsername(String username);

	/**
	 * Finds a Customer by customer ID.
	 *
	 * @param customerId the ID of the Customer to search for
	 * @return an Optional containing the Customer if found, or an empty Optional if not found
	 */
	Optional<Customer> findById(Long customerId);

	/**
	 * Finds all Customers in the database.
	 *
	 * @return a List of Customer objects
	 */
	List<Customer> findAllCustomers();

	/**
	 * Checks if a Customer with the given username exists in the database.
	 *
	 * @param username the username to check
	 * @return true if a Customer with the given username exists, false otherwise
	 */
	boolean existsByUsername(String username);

	/**
	 * Saves a new Customer to the database.
	 *
	 * @param customer the Customer object to save
	 * @return the saved Customer object
	 */
	Customer saveCustomer(Customer customer);

	/**
	 * Updates an existing Customer in the database.
	 *
	 * @param customer the Customer object to update
	 * @return the updated Customer object
	 */
	Customer updateCustomer(Customer customer);

	/**
	 * Deletes a Customer by username.
	 *
	 * @param username the username of the Customer to delete
	 * @return true if the Customer was deleted, false otherwise
	 */
	boolean deleteCustomerByUsername(String username);

	/**
	 * Deletes a Customer by customer ID.
	 *
	 * @param customerId the ID of the Customer to delete
	 * @return true if the Customer was deleted, false otherwise
	 */
	boolean deleteCustomerById(Long customerId);
}
