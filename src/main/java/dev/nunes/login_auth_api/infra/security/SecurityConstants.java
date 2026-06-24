package dev.nunes.login_auth_api.infra.security;

public class SecurityConstants {
    public static final long ACCESS_TOKEN_EXPIRATION = 600000; // 10 minutes in milliseconds
    public static final long REFRESH_TOKEN_EXPIRATION = 604800000; // 7 days in milliseconds
    public static final String TOKEN_TYPE = "Bearer";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_SUBJECT = "email";
}

