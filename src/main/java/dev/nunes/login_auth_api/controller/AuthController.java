package dev.nunes.login_auth_api.controller;

import dev.nunes.login_auth_api.domain.user.User;
import dev.nunes.login_auth_api.exception.InvalidCredentialsException;
import dev.nunes.login_auth_api.exception.UserAlreadyExistsException;
import dev.nunes.login_auth_api.infra.security.TokenService;
import dev.nunes.login_auth_api.repository.UserRepository;
import dev.nunes.login_auth_api.vo.request.LoginRequest;
import dev.nunes.login_auth_api.vo.request.RegisterRequest;
import dev.nunes.login_auth_api.vo.request.RefreshTokenRequest;
import dev.nunes.login_auth_api.vo.response.TokenResponse;
import dev.nunes.login_auth_api.vo.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and token management endpoints")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Operation(summary = "User login", description = "Authenticate user and generate access and refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getEmail());

        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null) {
            log.warn("Login failed: user not found - {}", loginRequest.getEmail());
            throw new UsernameNotFoundException("User not found.");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Login failed: invalid password for user - {}", loginRequest.getEmail());
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        log.info("Login successful for user: {}", loginRequest.getEmail());

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(600L) // 10 minutes
                .user(mapToUserResponse(user))
                .build();

        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary = "User registration", description = "Create a new user account and generate tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Register attempt for user: {}", registerRequest.getEmail());

        User existentUser = userRepository.findByEmail(registerRequest.getEmail());

        if (existentUser != null) {
            log.warn("Register failed: user already exists - {}", registerRequest.getEmail());
            throw new UserAlreadyExistsException("User with this email already exists.");
        }

        User newUser = new User();
        newUser.setName(registerRequest.getName());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        this.userRepository.save(newUser);

        log.info("New user registered successfully: {}", registerRequest.getEmail());

        String accessToken = tokenService.generateAccessToken(newUser);
        String refreshToken = tokenService.generateRefreshToken(newUser);

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(600L) // 10 minutes
                .user(mapToUserResponse(newUser))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse);
    }

    @Operation(summary = "Refresh access token", description = "Generate a new access token using a valid refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully", content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Refresh token attempt");

        String email = tokenService.validateToken(request.getRefreshToken());
        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.warn("Refresh failed: user not found");
            throw new UsernameNotFoundException("User not found.");
        }

        log.info("Token refreshed successfully for user: {}", email);

        String newAccessToken = tokenService.generateAccessToken(user);
        String newRefreshToken = tokenService.generateRefreshToken(user);

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(600L) // 10 minutes
                .user(mapToUserResponse(user))
                .build();

        return ResponseEntity.ok(tokenResponse);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
