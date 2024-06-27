package dev.abreu.bankapp.service;

import java.util.Map;
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
	 * Extracts username from jwt token
	 * 
	 * @param token
	 * @return
	 */
	public String extractUsername(String token);
}
