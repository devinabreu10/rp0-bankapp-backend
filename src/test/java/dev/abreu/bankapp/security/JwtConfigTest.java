package dev.abreu.bankapp.security;

import dev.abreu.bankapp.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtConfigTest {
    @InjectMocks
    private JwtConfig jwtConfig;

    @Mock
    private Customer customer;

    private final int expiration = 3600;

    private String sampleToken;

    @BeforeEach
    void setUp() {
        // base64 for 'secretKey1234567890123456789012345678901234567890'
        String secret = "c2VjcmV0S2V5MTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA==";
        ReflectionTestUtils.setField(jwtConfig, "salt", secret);
        ReflectionTestUtils.setField(jwtConfig, "expiration", expiration);
        // Generate a simple JWT token for tests
        Key key = jwtConfig.getSigningKey();
        sampleToken = Jwts.builder()
                .setSubject("testuser")
                .setExpiration(new Date(System.currentTimeMillis() + 3700000))
                .signWith(key)
                .compact();
    }

    @Test
    void testGetExpiration() {
        assertEquals(expiration, jwtConfig.getExpiration());
    }

    @Test
    void testExtractUsername() {
        String username = jwtConfig.extractUsername(sampleToken);
        assertEquals("testuser", username);
    }

    @Test
    void testExtractAllClaims() {
        Claims claims = jwtConfig.extractAllClaims(sampleToken);
        assertEquals("testuser", claims.getSubject());
    }

    @Test
    void testExtractClaim() {
        String username = jwtConfig.extractClaim(sampleToken, Claims::getSubject);
        assertEquals("testuser", username);
    }

    @Test
    void testGetSigningKey() {
        Key key = jwtConfig.getSigningKey();
        assertNotNull(key);
    }

    @Test
    void testIsTokenValid_Valid() {
        when(customer.getUsername()).thenReturn("testuser");
        boolean valid = jwtConfig.isTokenValid(sampleToken, customer) && jwtConfig.isTokenValidPastOneHour(sampleToken);
        assertTrue(valid);
    }

    @Test
    void testIsTokenValid_InvalidUsername() {
        when(customer.getUsername()).thenReturn("otheruser");
        boolean valid = jwtConfig.isTokenValid(sampleToken, customer);
        assertFalse(valid);
    }
}
