package dev.abreu.bankapp.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.abreu.bankapp.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtConfig {
	
	/**
	 * retrieves the jwt secret from environment variables
	 * 
	 */
	private String salt = System.getenv("JWT_SECRET");
	
	/**
	 * Calculating the number of milliseconds in a day using
	 * Spring expression language (SpEL)
	 * 
	 */
	@Value("#{24*60*60*1000}")
	private int expiration;

	/**
	 * 
	 * @return expiration time
	 */
	public int getExpiration() {
		return this.expiration;
	}
	
	/**
	 * checks if JWT belonging to customer is still valid
	 * 
	 * @param token
	 * @param customer
	 * @return
	 */
	public boolean isTokenValid(String token, Customer customer) {
		final String username = extractUsername(token);
		return (username.equals(customer.getUsername())) && !isTokenExpired(token);
	}

	/**
	 * @param token
	 * @return
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date(System.currentTimeMillis()));
	}

	/**
	 * @param token
	 * @return
	 */
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	/**
	 * extract username field from JWT token provided
	 * 
	 * @param token
	 * @return String username
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	/**
	 * extract all Claims for provided token
	 * 
	 * @param token
	 * @return
	 */
	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();	
	}
	
	/**
	 * extract one specific claim from Claims
	 * 
	 * @param <T>
	 * @param token
	 * @param claimsResolver
	 * @return
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	/**
	 * 
	 * @return jwt signing key
	 */
	public Key getSigningKey() {
		byte[] saltBytes = Decoders.BASE64.decode(salt);
		return Keys.hmacShaKeyFor(saltBytes);
	}
}
