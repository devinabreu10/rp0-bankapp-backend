package dev.abreu.bankapp.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.abreu.bankapp.dto.CustomerResponseDTO;
import dev.abreu.bankapp.dto.LoginRequest;
import dev.abreu.bankapp.dto.RegisterRequest;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import dev.abreu.bankapp.exception.UsernameTakenException;
import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.service.CustomerService;
import dev.abreu.bankapp.service.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private static final Logger log = LogManager.getLogger(AuthController.class);
	
	private final CustomerService customerService;
	private final TokenService tokenService;
	private final AuthenticationManager authenticationManager;
	private final DtoMapper dtoMapper;
	
	public AuthController(CustomerService customerService, TokenService tokenService,
			AuthenticationManager authenticationManager, DtoMapper dtoMapper) {
		this.customerService = customerService;
		this.tokenService = tokenService;
		this.authenticationManager = authenticationManager;
		this.dtoMapper = dtoMapper;
	}

	/**
	 * validates customer login using JWT
	 * 
	 * @param loginRequest
	 * @return
	 */
	@PostMapping(path = "/login")
	public ResponseEntity<CustomerResponseDTO> customerLogin(@RequestBody LoginRequest loginRequest) {
		log.info("Performing POST method to login Customer and generate JWT token");
		
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.username(), 
						loginRequest.password()));
		
		var customer = customerService.getCustomerByUsername(loginRequest.username());
		
		var jwt = tokenService.generateToken(customer);
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.AUTHORIZATION, jwt)
				.body(dtoMapper.toCustomerResponseDto(customer));
	}
	
	/**
	 * Performs POST method to register new customer
	 * 
	 * @param customer
	 * @return customer
	 * @throws UsernameTakenException 
	 */
	@PostMapping(path = "/register")
	public ResponseEntity<CustomerResponseDTO> registerCustomer(@RequestBody RegisterRequest registerRequest) 
			throws UsernameTakenException {
		log.info("Performing POST method to register new Customer and generate JWT token");
		
		Customer customer = dtoMapper.toCustomer(registerRequest);
		
		customerService.registerNewCustomer(customer);
		
		String jwt = tokenService.generateToken(customer);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.header(HttpHeaders.AUTHORIZATION, jwt)
				.body(dtoMapper.toCustomerResponseDto(customer));
	}
	
}
