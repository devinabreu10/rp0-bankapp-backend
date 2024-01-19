package dev.abreu.bankapp.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	
	
	
	
}
