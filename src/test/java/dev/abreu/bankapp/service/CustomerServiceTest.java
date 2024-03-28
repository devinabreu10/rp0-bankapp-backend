package dev.abreu.bankapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.exception.ResourceNotFoundException;
import dev.abreu.bankapp.exception.UsernameTakenException;
import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.service.impl.CustomerServiceImpl;

@SpringBootTest(classes = CustomerServiceImpl.class)
class CustomerServiceTest {
	
	@MockBean
	private CustomerDao customerDao;
	
	@MockBean
	private AccountDao accountDao;
	
	@Autowired
	private CustomerService customerService;

	@Test
	void testRegisterNewCustomer() throws UsernameTakenException {
		String testUsername = "test123";
		
		Customer mockCustomer = new Customer();
		Customer mockCustomerWithId = new Customer();
		mockCustomerWithId.setId(1L);
		
		Mockito.when(customerDao.existsByUsername(testUsername)).thenReturn(Boolean.FALSE);
		
		Mockito.when(customerDao.saveCustomer(mockCustomer)).thenReturn(mockCustomerWithId);
		
		Customer result = customerService.registerNewCustomer(mockCustomer);
		
		assertNotNull(result);
	}
	
	@Test
	void testRegisterNewCustomerWhenUsernameTaken() {
		String testUsername = "test123";
		Customer mockCustomer = new Customer();
		mockCustomer.setUsername(testUsername);
		
		Mockito.when(customerDao.existsByUsername(testUsername)).thenReturn(Boolean.TRUE);
		
		Mockito.when(customerDao.saveCustomer(mockCustomer)).thenReturn(mockCustomer);
		
		assertThrows(UsernameTakenException.class, 
				() -> customerService.registerNewCustomer(mockCustomer));
	}

	@Test
	void testGetCustomerByUsername() {
		String testUsername = "test123";
		Customer mockCustomer = new Customer();
		mockCustomer.setUsername(testUsername);
		
		Mockito.when(customerDao.findByUsername(testUsername)).thenReturn(Optional.of(mockCustomer));
		
		Customer result = customerService.getCustomerByUsername(testUsername);
		
		assertEquals(mockCustomer, result);
	}
	
	@Test
	void testGetCustomerByUsernameThrowsResourceNotFound() {
		String testUsername = "test123";
		
		Mockito.when(customerDao.findByUsername(testUsername)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, 
				() -> customerService.getCustomerByUsername(testUsername));
	}
	
	@Test
	void testGetCustomerById() {
		Long testCustomerId = 123L;
		Customer mockCustomer = new Customer();
		mockCustomer.setId(testCustomerId);
		
		Mockito.when(customerDao.findById(testCustomerId)).thenReturn(Optional.of(mockCustomer));
		
		Customer result = customerService.getCustomerById(testCustomerId);
		
		assertEquals(mockCustomer, result);
	}
	
	@Test
	void testGetCustomerByIdThrowsResourceNotFound() {
		Long testCustomerId = 123L;
		
		Mockito.when(customerDao.findById(testCustomerId)).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, 
				() -> customerService.getCustomerById(testCustomerId));
	}
	
	@Test
	void testGetAllCustomer() {
		List<Customer> mockCustomerList = new ArrayList<>();
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		Customer mockCustomerTwo = new Customer(2L, "testFirstTwo", "testLastTwo", "testAddrTwo", "testUsernameTwo");
		mockCustomerList.add(mockCustomer);
		mockCustomerList.add(mockCustomerTwo);
		
		Mockito.when(customerDao.findAllCustomers()).thenReturn(mockCustomerList);
		
		List<Customer> result = customerService.getAllCustomers();
		
		assertEquals(mockCustomerList, result);
	}

	@Test
	void testUpdateCustomerDetails() {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		Customer mockCustomerUpdate = new Customer(1L, "newTestFirst", "newTestLast", "newTestAddr", "testUsername");
		
		Mockito.when(customerDao.updateCustomer(mockCustomer)).thenReturn(mockCustomerUpdate);
		
		Customer result = customerService.updateCustomerDetails(mockCustomer);
		
		assertEquals(mockCustomerUpdate, result);
	}

	@Test
	void testDeleteCustomerByUsername() {
		String testUsername = "test123";
		Customer mockCustomer = new Customer();
		mockCustomer.setUsername(testUsername);
		
		Mockito.when(customerDao.existsByUsername(testUsername)).thenReturn(Boolean.TRUE);
		
		Mockito.when(customerDao.deleteCustomerByUsername(testUsername)).thenReturn(Boolean.TRUE);
		
		boolean result = customerService.deleteCustomerByUsername(testUsername);
		
		assertTrue(result);	
	}
	
	@Test
	void testDeleteCustomerByUsernameNotFound() {
		String testUsername = "test123";
		Customer mockCustomer = new Customer();
		mockCustomer.setUsername(testUsername);
		
		Mockito.when(customerDao.existsByUsername(testUsername)).thenReturn(Boolean.FALSE);
		
		Mockito.when(customerDao.deleteCustomerByUsername(testUsername)).thenReturn(Boolean.FALSE);
		
		boolean result = customerService.deleteCustomerByUsername(testUsername);
		
		assertFalse(result);	
	}
	
	@Test
	void testDeleteCustomerById() {
		Long testCustomerId = 123L;
		Customer mockCustomer = new Customer();
		mockCustomer.setId(testCustomerId);
		
		Mockito.when(customerDao.findById(testCustomerId)).thenReturn(Optional.of(mockCustomer));
		
		Mockito.when(customerDao.deleteCustomerById(testCustomerId)).thenReturn(Boolean.TRUE);
		
		boolean result = customerService.deleteCustomerById(testCustomerId);
		
		assertTrue(result);
	}
	
	@Test
	void testDeleteCustomerByIdNotFound() {
		Long testCustomerId = 123L;
		
		Mockito.when(customerDao.findById(testCustomerId)).thenReturn(Optional.empty());
		
		Mockito.when(customerDao.deleteCustomerById(testCustomerId)).thenReturn(Boolean.FALSE);
		
		boolean result = customerService.deleteCustomerById(testCustomerId);
		
		assertFalse(result);
	}

	@Test
	void testSuccessfulCustomerLogin() {
		String testUsername = "test123";
		String testPassword = "h2e3847%36";
		Optional<Customer> mockCustomer = Optional.of(new Customer());
		mockCustomer.orElseThrow().setUsername(testUsername);
		mockCustomer.orElseThrow().setPassword(testPassword);
		
		Mockito.when(customerDao.findByUsername(testUsername)).thenReturn(mockCustomer);
		
		Optional<Customer> result = customerService.customerLogin(testUsername, testPassword);
		
		assertEquals(mockCustomer, result);
	}
	
	@Test
	void testFailedCustomerLogin() {
		String testUsername = "test123";
		String testPassword = "h2e3847%36";
		Optional<Customer> mockCustomer = Optional.of(new Customer());
		mockCustomer.orElseThrow().setUsername("test456");
		mockCustomer.orElseThrow().setPassword("hd38764^e");
		
		Mockito.when(customerDao.findByUsername(testUsername)).thenReturn(Optional.empty());
		
		Optional<Customer> result = customerService.customerLogin(testUsername, testPassword);
		
		assertEquals(Optional.empty(), result);
	}
	
	@Test
	void testCustomerLoginWrongPassword() {
		String testUsername = "test123";
		String testPassword = "h2e3847%36";
		Optional<Customer> mockCustomer = Optional.of(new Customer());
		mockCustomer.orElseThrow().setUsername("test123");
		mockCustomer.orElseThrow().setPassword("hd38764^e");
		
		Mockito.when(customerDao.findByUsername(testUsername)).thenReturn(mockCustomer);
		
		Optional<Customer> result = customerService.customerLogin(testUsername, testPassword);
		
		assertEquals(Optional.empty(), result);
	}
	
	@Test
	void testCustomerLoginNullPassword() {
		String testUsername = "test123";
		String testPassword = null;
		Optional<Customer> mockCustomer = Optional.of(new Customer());
		mockCustomer.orElseThrow().setUsername("test123");
		mockCustomer.orElseThrow().setPassword("hd38764^e");
		
		Mockito.when(customerDao.findByUsername(testUsername)).thenReturn(mockCustomer);
		
		Optional<Customer> result = customerService.customerLogin(testUsername, testPassword);
		
		assertEquals(Optional.empty(), result);
	}
}
