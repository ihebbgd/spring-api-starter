package com.codewithmosh.store.Services;

import com.codewithmosh.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    /**
     * Generates a JWT token for the authenticated user.
     * 
     * Use Case: Creates a secure token that contains user identity information
     * and can be used for subsequent API requests without requiring credentials.
     * 
     * Token includes:
     * - User ID as subject for identification
     * - Email and name as custom claims for user context
     * - Issued at and expiration timestamps for token lifecycle
     * - HMAC-SHA signature for tamper protection
     * 
     * @param user The authenticated user entity
     * @return Signed JWT token valid for 24 hours
     */
    public String generateToken(User user){
        final long tokenExpiration =86400 ;//Seconds in one day
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("name",user.getName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    /**
     * Validates a JWT token's integrity and expiration.
     * 
     * Use Case: Security check before processing authenticated requests.
     * Ensures the token hasn't been tampered with and is still within its valid timeframe.
     * 
     * @param token The JWT token to validate
     * @return true if token is valid and not expired, false otherwise
     */
    public boolean validateToken(String token){
        try {
            var claims = getClaims(token);

            return claims.getExpiration().after(new Date());


        }
        catch (JwtException e){
            return false;}

    }

    /**
     * Extracts and parses claims from a JWT token.
     * 
     * Use Case: Internal helper method to decode token payload.
     * Uses the same secret key to verify the token's signature.
     * 
     * @param token The JWT token to parse
     * @return Claims object containing all token data
     * @throws JwtException if token is invalid or malformed
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts the user ID from a valid JWT token.
     * 
     * Use Case: Identifies the authenticated user for request processing.
     * The user ID is stored as the subject claim during token generation.
     * 
     * @param token The valid JWT token
     * @return The user ID extracted from the token
     */
    public Long getuserIdToken(String token){
        return Long.valueOf(getClaims(token).getSubject());

    }




}
