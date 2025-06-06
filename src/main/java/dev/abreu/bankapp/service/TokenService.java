package dev.abreu.bankapp.service;

import java.util.Map;
import java.util.Optional;

import dev.abreu.bankapp.entity.Customer;

public interface TokenService {
	
	/**
	 * Creates a new jwt token based on customers
	 * credentials
	 * 
	 * @param customer
	 * @return
	 */
    String generateToken(Customer customer);
	
	/**
	 * Creates a new jwt token based on customers
	 * credentials and extra claims
	 * 
	 * @param extraClaims
	 * @param customer
	 * @return
	 */
    String generateToken(Map<String, Object> extraClaims, Customer customer);
	
	/**
	 * Extracts username from jwt token
	 * 
	 * @param token
	 * @return
	 */
    String extractUsername(String token);

	/**
	 * Checks cached token is valid
	 *
	 * @param username - customer username
	 * @return - cached token if available or empty optional if not
	 */
	Optional<String> getCachedToken(String username);

	/**
	 * Checks if token is valid
	 *
	 * @param token - jwt token
	 * @return - true if token is valid (not expired)
	 */
	boolean isTokenValid(String token);
}
