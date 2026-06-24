package dev.nunes.login_auth_api.vo.response;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TokenResponseTest {

    @Test
    public void testTokenResponseBuild() {
        UserResponse userResponse = UserResponse.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .tokenType("Bearer")
                .expiresIn(600L)
                .user(userResponse)
                .build();

        assertNotNull(tokenResponse);
        assertEquals("access_token", tokenResponse.getAccessToken());
        assertEquals("refresh_token", tokenResponse.getRefreshToken());
        assertEquals("Bearer", tokenResponse.getTokenType());
        assertEquals(600L, tokenResponse.getExpiresIn());
        assertEquals("Test User", tokenResponse.getUser().getName());
    }

    @Test
    public void testUserResponseBuild() {
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        UserResponse userResponse = UserResponse.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertNotNull(userResponse);
        assertEquals(userId, userResponse.getId());
        assertEquals("Test User", userResponse.getName());
        assertEquals("test@example.com", userResponse.getEmail());
        assertEquals(now, userResponse.getCreatedAt());
        assertEquals(now, userResponse.getUpdatedAt());
    }
}

