package dev.abreu.bankapp.security;

import java.security.Key;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

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
	
	private Key signingKey;
	
	/**
	 * @PostContruct annotation means that the method should be 
	 * executed after dependency injection is complete
	 * 
	 */
	@PostConstruct
	public void createKey() {
		byte[] saltyBytes = Base64.getDecoder().decode(salt);
		signingKey = new SecretKeySpec(saltyBytes, SignatureAlgorithm.HS256.getJcaName());
	}
	
	/**
	 * 
	 * @return expiration time
	 */
	public int getExpiration() {
		return this.expiration;
	}
	
	/**
	 * Using RS256 algorithm which requires 540-character (2048 bit) key
	 * 
	 * @return jwt signature algorithm
	 */
	public SignatureAlgorithm getSigAlg() {
		return SignatureAlgorithm.HS256;
	}
	
	/**
	 * 
	 * @return jwt signing key
	 */
	public Key getSigningKey() {
		return this.signingKey;
	}
	
}
