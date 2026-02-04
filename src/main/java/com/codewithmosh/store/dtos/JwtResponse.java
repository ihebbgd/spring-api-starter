package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * JWT Response DTO for wrapping JWT tokens in API responses.
 * 
 * Use Cases:
 * - Standardize JWT token response format
 * - Provide consistent API contract for login endpoints
 * - Enable easy JSON serialization of tokens
 * 
 * This DTO ensures that JWT tokens are returned in a structured
 * format that can be easily consumed by client applications.
 */
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
}
