package dev.abreu.bankapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.exception.UsernameTakenException;
import dev.abreu.bankapp.entity.Customer;
import dev.abreu.bankapp.service.CustomerService;
import dev.abreu.bankapp.util.ResourceType;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	private static final Logger log = LogManager.getLogger(CustomerServiceImpl.class);
	
	private final CustomerDao customerDao;

	public CustomerServiceImpl(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@Override
	@CachePut(value = "customer", key = "#customer.username")
	public Customer registerNewCustomer(Customer customer) throws UsernameTakenException {
		boolean usernameExists = customerDao.existsByUsername(customer.getUsername());
		
		if(!usernameExists) {
			log.info("Registering new customer using unique username");
			customerDao.saveCustomer(customer);
		} else {
			log.error("Username is already present in the database");
			throw new UsernameTakenException();
		}
		
		return customer;
	}

	@Override
	@Cacheable(value = "customer", key = "#username")
	public Customer getCustomerByUsername(String username) {
		log.info("Fetching customer with username");
		return customerDao.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException(ResourceType.CUSTOMER, username));
	}
	
	@Override
	@Cacheable(value = "customer", key = "#customerId")
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
	@CachePut(value = "customer", key = "#customer.username")
	public Customer updateCustomerDetails(Customer customer) {
		log.info("Updating customer details using username");
		return customerDao.updateCustomer(customer);
	}
	
	@Override
	@CacheEvict(value = "customer", key = "#username")
	public boolean deleteCustomerByUsername(String username) {
		log.info("Deleting customer using username");
		
		boolean success = false;
		
		if(customerDao.existsByUsername(username)) {
			success = customerDao.deleteCustomerByUsername(username);
		}
		
		return success;
	}

	@Override
	@CacheEvict(value = "customer", key = "#customerId")
	public boolean deleteCustomerById(Long customerId) {
		log.info("Deleting customer with id: {}", customerId);
		
		boolean success = false;
		
		if(!customerDao.findById(customerId).equals(Optional.empty())) {
			success = customerDao.deleteCustomerById(customerId);
		}
		
		return success;
	}

}
