package dev.abreu.bankapp.security;

import dev.abreu.bankapp.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtConfig {

	/**
	 * Represents the secret key used for signing JWT tokens.
	 * This value is fetched from the application properties.
	 */
	@Value("${application.security.jwt.secret-key}")
	private String salt;

	/**
	 * The expiration time (in seconds) for JWT tokens.
	 * This value is fetched from the application properties.
	 */
	@Value("${application.security.jwt.expiration}")
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
	 * Check if token is valid for at least 1 hour
	 *
	 * @param token - jwt token
	 * @return boolean - true if token is valid for at least 1 hour
	 */
	public boolean isTokenValidPastOneHour(String token) {
		return extractExpiration(token).after(new Date(System.currentTimeMillis() + 3600000));
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
