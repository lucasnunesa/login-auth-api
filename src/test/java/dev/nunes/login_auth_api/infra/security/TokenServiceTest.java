package dev.nunes.login_auth_api.infra.security;

import dev.nunes.login_auth_api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashedpassword");
    }

    @Test
    public void testGenerateAccessToken() {
        String token = tokenService.generateAccessToken(testUser);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 0);
    }

    @Test
    public void testGenerateRefreshToken() {
        String token = tokenService.generateRefreshToken(testUser);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 0);
    }

    @Test
    public void testValidateAccessToken() {
        String token = tokenService.generateAccessToken(testUser);
        String email = tokenService.validateToken(token);

        assertEquals(testUser.getEmail(), email);
    }

    @Test
    public void testValidateRefreshToken() {
        String token = tokenService.generateRefreshToken(testUser);
        String email = tokenService.validateToken(token);

        assertEquals(testUser.getEmail(), email);
    }

    @Test
    public void testValidateInvalidToken() {
        String invalidToken = "invalid.token.here";

        assertThrows(Exception.class, () -> tokenService.validateToken(invalidToken));
    }
}

