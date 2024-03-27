package dev.abreu.bankapp.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.model.dto.CustomerDTO;
import dev.abreu.bankapp.service.CustomerService;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {
	
	@MockBean
	private CustomerService customerService;
	
	@MockBean
	private CustomerDao customerDao;
	
	@MockBean
	private AccountDao accountDao;

	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper jsonMapper = new ObjectMapper();

	@Test
	void testGetCustomerByUsername() throws JsonProcessingException, Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		CustomerDTO mockCustomerDto = new CustomerDTO(mockCustomer);
		
		Mockito.when(customerService.getCustomerByUsername(mockCustomer.getUsername())).thenReturn(mockCustomer);
		
		mockMvc.perform(get("/customer/get/user/testUsername"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDto)));
	}

	@Test
	void testGetCustomerById() throws JsonProcessingException, Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		CustomerDTO mockCustomerDto = new CustomerDTO(mockCustomer);

		Mockito.when(customerService.getCustomerById(mockCustomer.getId())).thenReturn(mockCustomer);

		mockMvc.perform(get("/customer/get/id/1"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDto)));
	}

	@Test
	void testGetAllCustomers() throws JsonProcessingException, Exception {
		List<Customer> mockCustomerList = new ArrayList<>();
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		Customer mockCustomerTwo = new Customer(2L, "testFirstTwo", "testLastTwo", "testAddrTwo", "testUsernameTwo");
		mockCustomerList.add(mockCustomer);
		mockCustomerList.add(mockCustomerTwo);
		
		List<CustomerDTO> mockCustomerDtoList = new ArrayList<>();
		mockCustomerList.stream().forEach(x -> mockCustomerDtoList.add(new CustomerDTO(x)));

		Mockito.when(customerService.getAllCustomers()).thenReturn(mockCustomerList);

		mockMvc.perform(get("/customer"))
				.andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDtoList)));
	}

	@Test
	void testSaveCustomer() throws JsonProcessingException, Exception {
		Customer mockCustomer = new Customer();
		Customer mockCustomerWithId = new Customer();
		mockCustomerWithId.setId(1L);
		
		CustomerDTO mockCustomerDto = new CustomerDTO();
		mockCustomerDto.setId(1L);

		Mockito.when(customerService.registerNewCustomer(mockCustomer)).thenReturn(mockCustomerWithId);

		mockMvc.perform(post("/customer/save")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockCustomerDto)))
				.andExpect(status().isCreated())
				.andReturn();
	}

	@Test
	void testUpdateCustomer() throws JsonProcessingException, Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		CustomerDTO mockCustomerDto = new CustomerDTO(mockCustomer);
		
		Mockito.when(customerService.updateCustomerDetails(any(Customer.class))).thenReturn(mockCustomer);
		
		mockMvc.perform(put("/customer/update/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockCustomerDto)))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDto)));
		
		verify(customerService, times(1)).updateCustomerDetails(any(Customer.class));
	}
	
	@Test
	void testUpdateCustomerConflict() throws JsonProcessingException, Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		Customer mockCustomerUpdate = new Customer(1L, "newTestFirst", "newTestLast", "newTestAddr", "testUsername");
		
		Mockito.when(customerService.updateCustomerDetails(mockCustomer)).thenReturn(mockCustomerUpdate);
		
		mockMvc.perform(put("/customer/update/2")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockCustomer)))
				.andExpect(status().isConflict());
	}
	
	@Test
	void testUpdateNullCustomer() throws JsonProcessingException, Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		CustomerDTO mockCustomerDto = new CustomerDTO(mockCustomer);
		
		Mockito.when(customerService.updateCustomerDetails(mockCustomer)).thenReturn(null);
		
		mockMvc.perform(put("/customer/update/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockCustomerDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testDeleteCustomerByUsername() throws JsonProcessingException, Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		
		Mockito.when(customerService.deleteCustomerByUsername(mockCustomer.getUsername())).thenReturn(Boolean.TRUE);
		
		mockMvc.perform(delete("/customer/delete/user/testUsername"))
				.andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test
	void testUnsucessfulDeleteCustomerByUsername() throws JsonProcessingException, Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		
		Mockito.when(customerService.deleteCustomerByUsername(mockCustomer.getUsername())).thenReturn(Boolean.FALSE);
		
		mockMvc.perform(delete("/customer/delete/user/testUsername"))
				.andExpect(status().isConflict())
				.andDo(print());
	}

	@Test
	void testDeleteCustomerById() throws Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		
		Mockito.when(customerService.deleteCustomerById(mockCustomer.getId())).thenReturn(Boolean.TRUE);
		
		mockMvc.perform(delete("/customer/delete/id/1"))
				.andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test
	void testUnsucessfulDeleteCustomerById() throws Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		
		Mockito.when(customerService.deleteCustomerById(mockCustomer.getId())).thenReturn(Boolean.FALSE);
		
		mockMvc.perform(delete("/customer/delete/id/1"))
				.andExpect(status().isConflict())
				.andDo(print());
	}

}
