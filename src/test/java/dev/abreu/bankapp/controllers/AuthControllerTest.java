package dev.abreu.bankapp.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.abreu.bankapp.config.ApplicationConfig;
import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.dto.CustomerResponseDTO;
import dev.abreu.bankapp.dto.LoginRequest;
import dev.abreu.bankapp.dto.RegisterRequest;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.security.JwtConfig;
import dev.abreu.bankapp.security.SecurityConfig;
import dev.abreu.bankapp.service.CustomerService;
import dev.abreu.bankapp.service.TokenService;

@WebMvcTest(controllers = AuthController.class)
@Import({SecurityConfig.class, ApplicationConfig.class})
class AuthControllerTest {

	@MockBean
	CustomerService customerService;
	
	@MockBean
	CustomerDao customerDao;
	
	@MockBean
	AccountDao accountDao;
	
	@MockBean
	TokenService tokenService;
	
	@MockBean
	JwtConfig jwtConfig;
	
	@MockBean
	DtoMapper dtoMapper;
	
	@MockBean
	PasswordEncoder passwordEncoder;
	
	@MockBean
	AuthenticationManager authenticationManager;
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper jsonMapper = new ObjectMapper();

	@Test
	void testCustomerLogin() throws JsonProcessingException, Exception {
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "user");
		CustomerResponseDTO mockCustomerDto = new CustomerResponseDTO("testFirst", "testLast", "user", "testAddr");
		LoginRequest mockRequest = new LoginRequest("user", "pass");
			
		Mockito.when(customerService.getCustomerByUsername("user")).thenReturn(mockCustomer);
		Mockito.when(dtoMapper.toCustomerResponseDto(mockCustomer)).thenReturn(mockCustomerDto);
		
		Mockito.when(tokenService.generateToken(mockCustomer)).thenReturn("token");
		
		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				 .content(jsonMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "token"))
                .andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDto)));
	}

	@Test
	void testRegisterCustomer() throws JsonProcessingException, Exception {
		Customer mockCustomer = new Customer("testFirst", "testLast", "testAddr", "user", "pass");
		CustomerResponseDTO mockCustomerDto = new CustomerResponseDTO("testFirst", "testLast", "user", "testAddr");
		RegisterRequest mockRequest = new RegisterRequest("testFirst", "testLast", "testAddr", "user", "pass");
		
		Mockito.when(customerService.registerNewCustomer(mockCustomer)).thenReturn(mockCustomer);
		Mockito.when(dtoMapper.toCustomer(mockRequest)).thenReturn(mockCustomer);
		Mockito.when(dtoMapper.toCustomerResponseDto(mockCustomer)).thenReturn(mockCustomerDto);
		
		Mockito.when(tokenService.generateToken(mockCustomer)).thenReturn("token");
		
		mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
				 .content(jsonMapper.writeValueAsString(mockRequest)))
               	.andExpect(status().isCreated())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDto)));
	}

}
