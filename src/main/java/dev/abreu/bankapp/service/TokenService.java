package dev.abreu.bankapp.service;

import java.util.Map;
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
}
