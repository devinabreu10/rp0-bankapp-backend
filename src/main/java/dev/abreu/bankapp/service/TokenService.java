package dev.abreu.bankapp.service;

import java.util.Optional;

import dev.abreu.bankapp.exception.TokenExpirationException;
import dev.abreu.bankapp.model.dto.CustomerDTO;

public interface TokenService {
	
	/**
	 * Creates a new jwt token based on customers
	 * credentials
	 * 
	 * @param customer
	 * @return
	 */
	public String createToken(CustomerDTO customer);

	/**
	 * Validates token provided to ensure token matches
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public Optional<CustomerDTO> validateToken(String token) throws TokenExpirationException;

	/**
	 * Retrieves default expiration value
	 * 
	 * @return
	 */
	public int getDefaultExpiration();
}
