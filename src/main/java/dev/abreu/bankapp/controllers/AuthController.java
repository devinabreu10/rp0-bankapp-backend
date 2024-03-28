package dev.abreu.bankapp.controllers;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.model.dto.CustomerDTO;
import dev.abreu.bankapp.model.dto.LoginRequest;
import dev.abreu.bankapp.service.CustomerService;
import dev.abreu.bankapp.service.TokenService;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/auth")
public class AuthController {
	
	private static final Logger log = LogManager.getLogger(AuthController.class);
	
	private final CustomerService customerService;
	private final TokenService tokenService;
	
	public AuthController(CustomerService customerService, TokenService tokenService) {
		this.customerService = customerService;
		this.tokenService = tokenService;
	}
	
	/**
	 * validates customer login using JWT
	 * 
	 * @param loginRequest
	 * @return
	 */
	@PostMapping(path = "/login")
	public ResponseEntity<CustomerDTO> customerLogin(@RequestBody LoginRequest loginRequest) {
		
		Optional<Customer> customerOpt = customerService
				.customerLogin(loginRequest.getUsername(), loginRequest.getPassword());
		
		if(customerOpt.isPresent()) {
			log.debug("Login was successful, creating token for Customer...");
			CustomerDTO customerDto = new CustomerDTO(customerOpt.orElseThrow());
			String jws = tokenService.createToken(customerDto);
			return ResponseEntity.status(HttpStatus.OK).header("Auth", jws).body(customerDto);
		} else {
			log.error("Login failed! Customer is unauthorized...");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	
	}
	
}
