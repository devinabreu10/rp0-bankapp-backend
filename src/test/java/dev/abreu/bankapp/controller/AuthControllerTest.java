package dev.abreu.bankapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.abreu.bankapp.config.ApplicationConfig;
import dev.abreu.bankapp.dao.AccountDao;
import dev.abreu.bankapp.dao.CustomerDao;
import dev.abreu.bankapp.dto.CustomerResponseDTO;
import dev.abreu.bankapp.dto.LoginRequest;
import dev.abreu.bankapp.dto.RegisterRequest;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import dev.abreu.bankapp.entity.Customer;
import dev.abreu.bankapp.security.JwtConfig;
import dev.abreu.bankapp.security.SecurityConfig;
import dev.abreu.bankapp.service.CustomerService;
import dev.abreu.bankapp.service.TokenService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
	
	private final ObjectMapper jsonMapper = new ObjectMapper();
	
	@Test
	void testGetAuthCustomer() throws Exception {
		String mockToken = "token";
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "user");
		CustomerResponseDTO mockCustomerDto = new CustomerResponseDTO(1L, "testFirst", "testLast", "user", "testAddr", mockToken);
		
		Mockito.when(customerService.getCustomerByUsername("user")).thenReturn(mockCustomer);
		Mockito.when(dtoMapper.toCustomerResponseDto(mockCustomer, mockToken)).thenReturn(mockCustomerDto);
		Mockito.when(tokenService.extractUsername(mockToken)).thenReturn(mockCustomer.getUsername());
		
		mockMvc.perform(get("/auth/user").contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer token"))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDto)));
	}

	@Test
	void testCustomerLogin() throws Exception {
		String mockToken = "token";
		Customer mockCustomer = new Customer(1L, "testFirst", "testLast", "testAddr", "user");
		CustomerResponseDTO mockCustomerDto = new CustomerResponseDTO(1L, "testFirst", "testLast", "user", "testAddr", mockToken);
		LoginRequest mockRequest = new LoginRequest("user", "pass");
			
		Mockito.when(customerService.getCustomerByUsername("user")).thenReturn(mockCustomer);
		Mockito.when(dtoMapper.toCustomerResponseDto(mockCustomer, mockToken)).thenReturn(mockCustomerDto);
		
		Mockito.when(tokenService.generateToken(mockCustomer)).thenReturn(mockToken);
		
		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				 .content(jsonMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, mockToken))
                .andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDto)));
	}

	@Test
	void testRegisterCustomer() throws Exception {
		String mockToken = "token";
		Customer mockCustomer = new Customer("testFirst", "testLast", "testAddr", "user", "password");
		CustomerResponseDTO mockCustomerDto = new CustomerResponseDTO(1L, "testFirst", "testLast", "user", "testAddr", mockToken);
		RegisterRequest mockRequest = new RegisterRequest("testFirst", "testLast", "testAddr", "user", "password");
		
		Mockito.when(customerService.registerNewCustomer(mockCustomer)).thenReturn(mockCustomer);
		Mockito.when(dtoMapper.toCustomer(mockRequest)).thenReturn(mockCustomer);
		Mockito.when(dtoMapper.toCustomerResponseDto(mockCustomer, mockToken)).thenReturn(mockCustomerDto);
		
		Mockito.when(tokenService.generateToken(mockCustomer)).thenReturn(mockToken);
		
		mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
				 .content(jsonMapper.writeValueAsString(mockRequest)))
               	.andExpect(status().isCreated())
                .andExpect(content().json(jsonMapper.writeValueAsString(mockCustomerDto)));
	}

}
