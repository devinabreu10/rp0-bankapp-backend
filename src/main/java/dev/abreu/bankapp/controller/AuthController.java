package dev.abreu.bankapp.controller;

import dev.abreu.bankapp.dto.CustomerResponseDTO;
import dev.abreu.bankapp.dto.LoginRequest;
import dev.abreu.bankapp.dto.RegisterRequest;
import dev.abreu.bankapp.dto.mapper.DtoMapper;
import dev.abreu.bankapp.entity.Customer;
import dev.abreu.bankapp.exception.UsernameTakenException;
import dev.abreu.bankapp.service.CustomerService;
import dev.abreu.bankapp.service.TokenService;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
	 * returns current authenticated Customer
	 * 
	 * @param authorizationHeader
	 * @return
	 */
	@GetMapping(path = "/user")
	public ResponseEntity<CustomerResponseDTO> getAuthenticatedCustomer(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
		log.info("Performing GET method for current authenticated Customer");

		String jwt = null;
		if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
		}

		String username = tokenService.extractUsername(jwt);

		log.info("Authenticated Customer: {}", username);

		var customer = customerService.getCustomerByUsername(username);

		return ResponseEntity.status(HttpStatus.OK)
				.body(dtoMapper.toCustomerResponseDto(customer, jwt));
	}

	/**
	 * validates customer login using JWT
	 * 
	 * @param loginRequest
	 * @return
	 */
	@PostMapping(path = "/login")
	public ResponseEntity<CustomerResponseDTO> customerLogin(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("Performing POST method to login Customer and generate JWT token");

		// Check cache for existing valid token
		Optional<String> cachedToken = tokenService.getCachedToken(loginRequest.username());
		if (cachedToken.isPresent() && tokenService.isTokenValid(cachedToken.orElseThrow())) {
			log.info("Using valid cached token for customer");
			var customer = customerService.getCustomerByUsername(loginRequest.username());
			return ResponseEntity.status(HttpStatus.OK)
					.header(HttpHeaders.AUTHORIZATION, cachedToken.orElseThrow())
					.body(dtoMapper.toCustomerResponseDto(customer, cachedToken.orElseThrow()));
		}

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.username(), 
						loginRequest.password()));
		
		var customer = customerService.getCustomerByUsername(loginRequest.username());
		
		var jwt = tokenService.generateToken(customer);
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.AUTHORIZATION, jwt)
				.body(dtoMapper.toCustomerResponseDto(customer, jwt));
	}
	
	/**
	 * Performs POST method to register new customer
	 * 
	 * @param customer
	 * @return customer
	 * @throws UsernameTakenException 
	 */
	@PostMapping(path = "/register")
	public ResponseEntity<CustomerResponseDTO> registerCustomer(@Valid @RequestBody RegisterRequest registerRequest) 
			throws UsernameTakenException {
		log.info("Performing POST method to register new Customer and generate JWT token");
		
		Customer customer = dtoMapper.toCustomer(registerRequest);
		
		customerService.registerNewCustomer(customer);
		
		String jwt = tokenService.generateToken(customer);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.header(HttpHeaders.AUTHORIZATION, jwt)
				.body(dtoMapper.toCustomerResponseDto(customer, jwt));
	}
	
}
