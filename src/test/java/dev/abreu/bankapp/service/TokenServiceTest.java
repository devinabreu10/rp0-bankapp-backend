package dev.abreu.bankapp.service;

import dev.abreu.bankapp.entity.Customer;
import dev.abreu.bankapp.security.JwtConfig;
import dev.abreu.bankapp.service.impl.TokenServiceImpl;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TokenServiceImpl.class)
class TokenServiceTest {

	@MockitoBean
	private JwtConfig jwtConfig;
	
	@Autowired
	private TokenService tokenService;

	@Test
	void testGenerateTokenCustomer() {
		Customer mockCustomer = new Customer();
		mockCustomer.setId(1L);
		byte[] mockBytes = new byte[256];
		
        Random random = new Random();
        random.nextBytes(mockBytes);
		
		Mockito.when(jwtConfig.getSigningKey()).thenReturn(Keys.hmacShaKeyFor(mockBytes));
		
		String jwt = tokenService.generateToken(mockCustomer);
		
		assertNotNull(jwt);
	}

	@Test
	void testTokenService() {
		String token = "test";
		Customer mockCustomer = new Customer();
		mockCustomer.setUsername("test");

		Mockito.when(jwtConfig.extractUsername(Mockito.anyString())).thenReturn("test");
		Mockito.when(jwtConfig.isTokenValidPastOneHour(Mockito.anyString())).thenReturn(true);

		boolean valid = tokenService.isTokenValid(token);
		String username = tokenService.extractUsername(token);
		String jwt = String.valueOf(tokenService.getCachedToken(mockCustomer.getUsername()));

		assertNotNull(jwt);
		assertEquals("test",username);
		assertTrue(valid);
	}

}
