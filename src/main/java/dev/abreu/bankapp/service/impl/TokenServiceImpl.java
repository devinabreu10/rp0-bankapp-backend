package dev.abreu.bankapp.service.impl;

import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import dev.abreu.bankapp.exception.TokenExpirationException;
import dev.abreu.bankapp.model.dto.CustomerDTO;
import dev.abreu.bankapp.security.JwtConfig;
import dev.abreu.bankapp.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class TokenServiceImpl implements TokenService {
	
	private static final Logger log = LogManager.getLogger(TokenServiceImpl.class);
	
	private final JwtConfig jwtConfig;
	
	public TokenServiceImpl(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@Override
	public String createToken(CustomerDTO customer) {
		String jws = "";
		
		if(customer != null && customer.getUsername() != null) {
			
			jws = Jwts.builder()
					.setId(String.valueOf(customer.getId()))
					.setSubject(customer.getUsername())
					.claim("role", "user")
					.setIssuer("rp0-bankapp")
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
					.signWith(jwtConfig.getSigningKey())
					.compact();
		}
		
		return jws;
	}

	@Override
	public Optional<CustomerDTO> validateToken(String token) throws TokenExpirationException {
		try {
			Claims jwtClaims = extractAllClaims(token);
			
			if(jwtClaims.getExpiration().before(new Date(System.currentTimeMillis()))) {
				throw new TokenExpirationException();
			}
			
			CustomerDTO customerDto = parseCustomer(jwtClaims);
			
			return Optional.of(customerDto);
			
		} catch(JwtException e) {
			log.error("JwtException: ", e);
		}
		
		return Optional.empty();
	}
	
	@Override
	public int getDefaultExpiration() {
		return jwtConfig.getExpiration();
	}

	private CustomerDTO parseCustomer(Claims jwtClaims) {
		Long id = Long.parseLong(jwtClaims.getId());
		String username = jwtClaims.getSubject();
		
		CustomerDTO customerDto = new CustomerDTO();
		customerDto.setId(id);
		customerDto.setUsername(username);
		
		return customerDto;
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(jwtConfig.getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();	
	}

}
