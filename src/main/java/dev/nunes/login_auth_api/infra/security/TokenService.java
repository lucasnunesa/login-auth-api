package dev.nunes.login_auth_api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.nunes.login_auth_api.domain.user.User;
import dev.nunes.login_auth_api.exception.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${spring.application.name}")
    private String apiName;

    public String generateAccessToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            log.info("Generating access token for user: {}", user.getEmail());

            String token = JWT.create()
                    .withIssuer(apiName)
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateAccessTokenExpirationDate())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            log.error("Error while generating access token", exception);
            throw new RuntimeException("Error while generating access token", exception);
        }
    }

    public String generateRefreshToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            log.info("Generating refresh token for user: {}", user.getEmail());

            String token = JWT.create()
                    .withIssuer(apiName)
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateRefreshTokenExpirationDate())
                    .withClaim("type", "refresh")
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            log.error("Error while generating refresh token", exception);
            throw new RuntimeException("Error while generating refresh token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            log.debug("Validating token");

            return JWT.require(algorithm)
                    .withIssuer(apiName)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            log.warn("Invalid token: {}", e.getMessage());
            throw new TokenExpiredException("Invalid or expired token");
        }
    }

    private Instant generateAccessTokenExpirationDate() {
        ZoneId saopaulo = ZoneId.of("America/Sao_Paulo");
        return LocalDateTime.now(saopaulo)
                .plusMinutes(10)
                .atZone(saopaulo)
                .toInstant();
    }

    private Instant generateRefreshTokenExpirationDate() {
        ZoneId saopaulo = ZoneId.of("America/Sao_Paulo");
        return LocalDateTime.now(saopaulo)
                .plusDays(7)
                .atZone(saopaulo)
                .toInstant();
    }
}
