package dev.abreu.bankapp.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import dev.abreu.bankapp.entity.Customer;
import dev.abreu.bankapp.security.JwtConfig;
import dev.abreu.bankapp.service.impl.TokenServiceImpl;
import io.jsonwebtoken.security.Keys;

@SpringBootTest(classes = TokenServiceImpl.class)
class TokenServiceTest {
	
	@MockBean
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

}
