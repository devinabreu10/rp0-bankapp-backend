package dev.abreu.bankapp.service.impl;

import static dev.abreu.bankapp.utils.BankappConstants.JWT_RP0_BANKAPP_ISSUER;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import dev.abreu.bankapp.dto.CustomerDTO;
import dev.abreu.bankapp.exception.TokenExpirationException;
import dev.abreu.bankapp.model.Customer;
import dev.abreu.bankapp.security.JwtConfig;
import dev.abreu.bankapp.service.TokenService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenServiceImpl implements TokenService {
	
	private static final Logger log = LogManager.getLogger(TokenServiceImpl.class);
	
	private final JwtConfig jwtConfig;
	
	public TokenServiceImpl(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}
	
	@Override
	public String generateToken(Customer customer) {
		return generateToken(new HashMap<>(), customer);
	}
	
	@Override
	public String generateToken(Map<String, Object> extraClaims, Customer customer) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(customer.getUsername())
				.setIssuer(JWT_RP0_BANKAPP_ISSUER)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
				.signWith(jwtConfig.getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	@Override
	public Optional<CustomerDTO> validateToken(String token, Customer customer) throws TokenExpirationException {
		try {
			
			if(!jwtConfig.isTokenValid(token, customer)) {
				throw new TokenExpirationException();
			}
			
			CustomerDTO customerDto = new CustomerDTO();
			customerDto.setUsername(jwtConfig.extractUsername(token));
			
			return Optional.of(customerDto);
			
		} catch(JwtException e) {
			log.error("JwtException: ", e);
		}
		
		return Optional.empty();
	}

}
