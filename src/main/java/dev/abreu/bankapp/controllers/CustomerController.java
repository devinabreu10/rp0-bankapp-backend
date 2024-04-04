package dev.abreu.bankapp.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.abreu.bankapp.dto.CustomerDTO;
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
	public ResponseEntity<CustomerDTO> getCustomerByUsername(@PathVariable("username") String username) {
		log.info("Performing GET method from inside getCustomerByUsername in CustomerController");
		
		Customer customer = customerService.getCustomerByUsername(username);
		
		CustomerDTO customerDto = new CustomerDTO(customer);
		
		log.info("Customer with username {} was successfully retrieved...", username);
		return ResponseEntity.ok(customerDto); //sends 200 status
	}
	
	
	/**
	 * Retrieves a Customer based on their id
	 * 
	 * @param id
	 * @return Customer
	 */
	@GetMapping("/get/id/{id}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("id") Long id) {
		log.info("Performing GET method from inside getCustomerById in CustomerController");
		
		Customer customer = customerService.getCustomerById(id);
		
		CustomerDTO customerDto = new CustomerDTO(customer);
		
		log.info("Customer with id {} was successfully retrieved...", id);
		return ResponseEntity.ok(customerDto);
	}
	
	/**
	 * Retrieves a list of all customers available in the database
	 * through an http GET method
	 * 
	 * @return a list of customers
	 */
	@GetMapping()
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
		log.info("Performing GET method from inside getAllCustomers()");
		
		List<Customer> customerList = customerService.getAllCustomers();
		
		List<CustomerDTO> customerDtoList = new ArrayList<>();
		customerList.stream().forEach(x -> customerDtoList.add(new CustomerDTO(x)));
		
		return ResponseEntity.ok(customerDtoList);
	}
	
	/**
	 * Update customer details by id
	 * 
	 * @param id
	 * @param customer
	 * @return customer
	 */
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerDTO customerDto) {
		log.info("Performing PUT method to update details for customer with id: {}", id);
		
		if(!customerDto.getId().equals(id)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
		Customer customer = customerDto.toEntity();
		
		customer = customerService.updateCustomerDetails(customer);
		
		if(customer != null) {
			return ResponseEntity.ok(customerDto);
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
