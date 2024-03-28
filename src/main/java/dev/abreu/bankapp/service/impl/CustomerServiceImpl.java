package dev.abreu.bankapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.exception.UsernameTakenException;
import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.service.CustomerService;
import dev.abreu.bankapp.utils.ResourceType;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	private static final Logger log = LogManager.getLogger(CustomerServiceImpl.class);
	
	private CustomerDao customerDao;

	public CustomerServiceImpl(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@Override
	public Customer registerNewCustomer(Customer customer) throws UsernameTakenException {
		
		boolean usernameExists = customerDao.existsByUsername(customer.getUsername());
		
		if(!usernameExists) {
			log.info("Registering new customer with username: {}", customer.getUsername());
			customerDao.saveCustomer(customer);
		} else {
			log.error("Username, {}, is already present in the database", customer.getUsername());
			throw new UsernameTakenException();
		}
		
		return customer;
	}

	@Override
	public Customer getCustomerByUsername(String username) {
		log.info("Fetching customer with username: {}", username);
		return customerDao.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(ResourceType.CUSTOMER, username));
	}
	
	@Override
	public Customer getCustomerById(Long customerId) {
		log.info("Fetching customer with Id: {}", customerId);
		return customerDao.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException(ResourceType.CUSTOMER, customerId));
	}
	
	@Override
	public List<Customer> getAllCustomers() {
		log.info("Fetching all customers...");
		return customerDao.findAllCustomers();
	}

	@Override
	public Customer updateCustomerDetails(Customer customer) {
		log.info("Updating customer details for username: {}", customer.getUsername());
		return customerDao.updateCustomer(customer);
	}
	
	@Override
	public boolean deleteCustomerByUsername(String username) {
		log.info("Deleting customer with username: {}", username);
		
		boolean success = false;
		
		if(customerDao.existsByUsername(username)) {
			success = customerDao.deleteCustomerByUsername(username);
		}
		
		return success;
	}

	@Override
	public boolean deleteCustomerById(Long customerId) {
		log.info("Deleting customer with id: {}", customerId);
		
		boolean success = false;
		
		if(!customerDao.findById(customerId).equals(Optional.empty())) {
			success = customerDao.deleteCustomerById(customerId);
		}
		
		return success;
	}

	@Override
	public Optional<Customer> customerLogin(String username, String password) {
		log.info("Attempting to sign in customer with username: {}", username);
		Optional<Customer> customer = customerDao.findByUsername(username);
		
		if(customer.isPresent() && (password != null && password.equals(customer.get().getPassword()))) {
			return customer;
		}
		
		return Optional.empty();
	}

}
