package dev.abreu.bankapp.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.abreu.bankapp.exception.UsernameTakenException;
import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.service.CustomerService;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {
	
	private static final Logger log = LogManager.getLogger(CustomerController.class);
	
	private CustomerService customerService;

	public CustomerController(CustomerService customerService) { //dependency injection for CustomerService 
		this.customerService = customerService;
	}

	/**
	 * Retrieves a Customer based on their username
	 * 
	 * @param username
	 * @return customer
	 */
	@GetMapping(path = "/get/{username}")
	public ResponseEntity<Customer> getCustomerByUsername(@PathVariable("username") String username) {
		log.info("Performing GET method from inside getCustomerByUsername()");
		
		Customer customer = customerService.getCustomerByUsername(username);
		
		if(customer != null) {
			log.info("The following customer was successfully retrieved: {}", customer);
			return ResponseEntity.ok(customer); //sends 200 status
		} else {
			log.info("Customer with the following username: {}, does not exist", username);
			return ResponseEntity.notFound().build(); //sends 404 status
		}
	}
	
	/**
	 * Retrieves a list of all customers available in the database
	 * through an http GET method
	 * 
	 * @return a list of customers
	 */
	@GetMapping()
	public List<Customer> getAllCustomers() {
		log.info("Performing GET method from inside getAllCustomers()");
		return customerService.getAllCustomers();
	}

	/**
	 * Performs POST method to register new customer
	 * 
	 * @param customer
	 * @return customer
	 */
	@PostMapping(path = "/save")
	public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) {
		log.info("Performing POST method to save new Customer: {}", customer);
		
		try {
			customer = customerService.registerNewCustomer(customer);
		} catch(UsernameTakenException e) {
			log.error("Username already exists...", e);
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(customer);
	}
	
	/**
	 * Update customer details by id
	 * (TODO: Finish this method)
	 * 
	 * @param id
	 * @param customer
	 * @return customer
	 */
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
		log.info("Performing PUT method to update details for customer with id: {}", id);
		
		return null;
	}
	

	/**
	 * Remove customer from database by id 
	 * (TODO: Finish this method)
	 * 
	 * @param id
	 */
	@DeleteMapping(path = "/delete/{id}")
	public void deleteCustomer(@PathVariable("id") long id) {
		log.info("Performing DELETE method for customer with id: {}", id);
		
		customerService.deleteCustomer(id);
		
	}
	
	
	
	
	
}
