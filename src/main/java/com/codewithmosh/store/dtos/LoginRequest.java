package com.codewithmosh.store.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Login Request DTO for handling user authentication credentials.
 * 
 * Use Cases:
 * - Validate user input during login process
 * - Ensure required fields are provided
 * - Validate email format
 * - Provide type-safe credential handling
 * 
 * This DTO encapsulates login credentials with built-in validation
 * to ensure data integrity before authentication processing.
 */
@Data
public class LoginRequest {
    /**
     * User email address for authentication.
     * Must be a valid email format and cannot be empty.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    /**
     * User password for authentication.
     * Cannot be empty or null.
     */
    @NotBlank(message = "Password is required")
    private String password;

}
