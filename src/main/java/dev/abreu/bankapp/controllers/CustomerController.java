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
	@GetMapping(path = "/get/user/{username}")
	public ResponseEntity<Customer> getCustomerByUsername(@PathVariable("username") String username) {
		log.info("Performing GET method from inside getCustomerByUsername in CustomerController");
		
		Customer customer = customerService.getCustomerByUsername(username);
		
		log.info("Customer with username {} was successfully retrieved...", username);
//		CustomerDTO dto = mapper.toDto(customer);
		return ResponseEntity.ok(customer); //sends 200 status
	}
	
	
	/**
	 * Retrieves a Customer based on their id
	 * 
	 * @param id
	 * @return Customer
	 */
	@GetMapping("/get/id/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id) {
		log.info("Performing GET method from inside getCustomerById in CustomerController");
		
		Customer customer = customerService.getCustomerById(id);
		
		log.info("Customer with id {} was successfully retrieved...", id);
		return ResponseEntity.ok(customer);
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
	 * @throws UsernameTakenException 
	 */
	@PostMapping(path = "/save")
	public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) throws UsernameTakenException {
		log.info("Performing POST method to save new Customer");
		
		//make sure that both DTO and domain don't need to know about each other by using a mapper class
//		Customer customer = mapper.toCustomer(customerDTO);

		customer = customerService.registerNewCustomer(customer);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(customer);
	}
	
	/**
	 * Update customer details by id
	 * 
	 * @param id
	 * @param customer
	 * @return customer
	 */
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customer) {
		log.info("Performing PUT method to update details for customer with id: {}", id);
		
		if(customer.getId() != id) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
		customer = customerService.updateCustomerDetails(customer);
		
		if(customer != null) {
			return ResponseEntity.ok(customer);
		} else {
			return ResponseEntity.badRequest().build();
		}

	}
	
	/**
	 * Remove customer from database by username
	 * 
	 * @param username
	 */
	@DeleteMapping(path = "/delete/user/{username}")
	public ResponseEntity<String> deleteCustomerByUsername(@PathVariable("username") String username) {
		log.info("Performing DELETE method for customer with username: {}", username);
		
		boolean success = customerService.deleteCustomerByUsername(username);
		
		if(Boolean.TRUE.equals(success)) {
			return new ResponseEntity<>("Customer successfully deleted...", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Customer could not be deleted, please check backend...", HttpStatus.CONFLICT);
		}
	}
	

	/**
	 * Remove customer from database by id 
	 * 
	 * @param id
	 */
	@DeleteMapping(path = "/delete/id/{id}")
	public ResponseEntity<String> deleteCustomerById(@PathVariable("id") Long id) {
		log.info("Performing DELETE method for customer with id: {}", id);
		
		boolean success = customerService.deleteCustomerById(id);
		
		if(Boolean.TRUE.equals(success)) {
			return new ResponseEntity<>("Customer successfully deleted...", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Customer could not be deleted, please check backend...", HttpStatus.CONFLICT);
		}
	}
	
}
