package dev.abreu.bankapp.service;

import java.util.Map;
import java.util.Optional;

import dev.abreu.bankapp.dto.CustomerDTO;
import dev.abreu.bankapp.exception.TokenExpirationException;
import dev.abreu.bankapp.model.Customer;

public interface TokenService {
	
	/**
	 * Creates a new jwt token based on customers
	 * credentials
	 * 
	 * @param customer
	 * @return
	 */
	public String generateToken(Customer customer);
	
	/**
	 * Creates a new jwt token based on customers
	 * credentials and extra claims
	 * 
	 * @param extraClaims
	 * @param customer
	 * @return
	 */
	public String generateToken(Map<String, Object> extraClaims, Customer customer);

	/**
	 * Validates token provided to ensure token matches
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public Optional<CustomerDTO> validateToken(String token, Customer customer) throws TokenExpirationException;
}
