package dev.abreu.bankapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.abreu.bankapp.config.ApplicationConfig;
import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.dto.CustomerDTO;
import dev.abreu.bankapp.dto.CustomerResponseDTO;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import dev.abreu.bankapp.entity.Customer;
import dev.abreu.bankapp.security.JwtConfig;
import dev.abreu.bankapp.security.SecurityConfig;
import dev.abreu.bankapp.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({ SecurityConfig.class, ApplicationConfig.class })
class CustomerControllerTest {

	@MockBean
	private CustomerService customerService;

	@MockBean
	private CustomerDao customerDao;

	@MockBean
	private AccountDao accountDao;

	@MockBean
	JwtConfig jwtConfig;
	
	@MockBean
	DtoMapper dtoMapper;

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper jsonMapper = new ObjectMapper();

	@Test
	void testGetCustomerByUsername() throws Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		CustomerResponseDTO mockCustomerDto = new CustomerResponseDTO(1L, "testFirst", "testLast", "testAddr", "testUsername", null);

		Mockito.when(customerService.getCustomerByUsername(mockCustomer.getUsername())).thenReturn(mockCustomer);
		Mockito.when(dtoMapper.toCustomerResponseDto(mockCustomer)).thenReturn(mockCustomerDto);

		mockMvc.perform(get("/customer/get/user/testUsername")).andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDto)));
	}

	@Test
	void testGetCustomerById() throws Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		CustomerResponseDTO mockCustomerDto = new CustomerResponseDTO(1L, "testFirst", "testLast", "testAddr", "testUsername", null);

		Mockito.when(customerService.getCustomerById(mockCustomer.getId())).thenReturn(mockCustomer);
		Mockito.when(dtoMapper.toCustomerResponseDto(mockCustomer)).thenReturn(mockCustomerDto);

		mockMvc.perform(get("/customer/get/id/1")).andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDto)));
	}

	@Test
	void testGetAllCustomers() throws Exception {
		List<Customer> mockCustomerList = new ArrayList<>();
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		CustomerResponseDTO mockCustomerDto = new CustomerResponseDTO(1L, "testFirst", "testLast", "testAddr", "testUsername", null);
		mockCustomerList.add(mockCustomer);

		List<CustomerResponseDTO> mockCustomerDtoList = new ArrayList<>();
		mockCustomerList.forEach(c -> mockCustomerDtoList
				.add(new CustomerResponseDTO(c.getId(), c.getFirstName(), c.getLastName(), c.getAddress(), c.getUsername(), null)));

		Mockito.when(customerService.getAllCustomers()).thenReturn(mockCustomerList);
		Mockito.when(dtoMapper.toCustomerResponseDto(mockCustomer)).thenReturn(mockCustomerDto);

		mockMvc.perform(get("/customer"))
				.andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDtoList)));
	}

	@Test
	void testUpdateCustomer() throws Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		CustomerDTO mockCustomerDto = new CustomerDTO(1L, "testFirst", "testLast", "testAddr", "testUsername");

		Mockito.when(customerService.updateCustomerDetails(any(Customer.class))).thenReturn(mockCustomer);
		Mockito.when(dtoMapper.toCustomer(mockCustomerDto)).thenReturn(mockCustomer);

		mockMvc.perform(put("/customer/update/1").contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockCustomerDto))).andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDto)));

		verify(customerService, times(1)).updateCustomerDetails(any(Customer.class));
	}

	@Test
	void testUpdateCustomerConflict() throws Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		Customer mockCustomerUpdate = new Customer(1L, "newTestFirst", "newTestLast", "newTestAddr", "testUsername");

		Mockito.when(customerService.updateCustomerDetails(mockCustomer)).thenReturn(mockCustomerUpdate);

		mockMvc.perform(put("/customer/update/2").contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockCustomer))).andExpect(status().isConflict());
	}

	@Test
	void testUpdateNullCustomer() throws Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");
		CustomerDTO mockCustomerDto = new CustomerDTO(1L, "testFirst", "testLast", "testAddr", "testUsername");

		Mockito.when(customerService.updateCustomerDetails(mockCustomer)).thenReturn(null);

		mockMvc.perform(put("/customer/update/1").contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockCustomerDto))).andExpect(status().isBadRequest());
	}

	@Test
	void testDeleteCustomerByUsername() throws Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");

		Mockito.when(customerService.deleteCustomerByUsername(mockCustomer.getUsername())).thenReturn(Boolean.TRUE);

		mockMvc.perform(delete("/customer/delete/user/testUsername")).andExpect(status().isOk()).andDo(print());
	}

	@Test
	void testUnsucessfulDeleteCustomerByUsername() throws Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");

		Mockito.when(customerService.deleteCustomerByUsername(mockCustomer.getUsername())).thenReturn(Boolean.FALSE);

		mockMvc.perform(delete("/customer/delete/user/testUsername")).andExpect(status().isConflict()).andDo(print());
	}

	@Test
	void testDeleteCustomerById() throws Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");

		Mockito.when(customerService.deleteCustomerById(mockCustomer.getId())).thenReturn(Boolean.TRUE);

		mockMvc.perform(delete("/customer/delete/id/1")).andExpect(status().isOk()).andDo(print());
	}

	@Test
	void testUnsuccessfulDeleteCustomerById() throws Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "testUsername");

		Mockito.when(customerService.deleteCustomerById(mockCustomer.getId())).thenReturn(Boolean.FALSE);

		mockMvc.perform(delete("/customer/delete/id/1")).andExpect(status().isConflict()).andDo(print());
	}

}
