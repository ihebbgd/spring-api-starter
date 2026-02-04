package com.codewithmosh.store.controllers;


import com.codewithmosh.store.Services.JwtService;
import com.codewithmosh.store.dtos.JwtResponse;
import com.codewithmosh.store.dtos.LoginRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.mappres.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller for handling user authentication operations.
 * 
 * Use Cases:
 * - User login with email/password credentials
 * - JWT token validation
 * - Retrieving current authenticated user information
 * - Error handling for authentication failures
 * 
 * Provides RESTful endpoints for the authentication flow in the application.
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService JwtService;
    private  final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Authenticates user credentials and returns JWT token.
     * 
     * Use Case: Main login endpoint for user authentication.
     * Validates credentials against database and generates JWT token
     * for subsequent authenticated requests.
     * 
     * Process:
     * 1. Authenticate email/password using AuthenticationManager
     * 2. Retrieve user from database
     * 3. Generate JWT token with user information
     * 4. Return token in response
     * 
     * @param loginRequest User credentials (email, password)
     * @return JWT token wrapped in JwtResponse
     * @throws Exception If authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        var user=userRepository.findByEmail(loginRequest.getEmail());
        var token=JwtService.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token));
    }
    /**
     * Validates a JWT token from Authorization header.
     * 
     * Use Case: Utility endpoint to check if a token is still valid.
     * Useful for client-side token validation before making authenticated requests.
     * 
     * @param authHeader Authorization header containing "Bearer <token>"
     * @return true if token is valid, false otherwise
     */
    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader){
        var token =authHeader.replace("Bearer ","");
        return JwtService.validateToken(token);
    }

    /**
     * Retrieves current authenticated user information.
     * 
     * Use Case: Get user profile/details for the currently logged-in user.
     * Uses the security context to identify the user from JWT token.
     * 
     * Process:
     * 1. Extract user ID from security context (set by JWT filter)
     * 2. Fetch user details from database
     * 3. Convert to DTO for safe response
     * 
     * @return UserDto with current user information, or 404 if user not found
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        var authentification=SecurityContextHolder.getContext().getAuthentication();
        var userId=(Long)authentification.getPrincipal();

        var user=userRepository.findById(userId).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        var userDto=userMapper.userToUserDto(user);
        return  ResponseEntity.ok(userDto);
    }





    /**
     * Global exception handler for authentication-related errors.
     * 
     * Use Case: Provides consistent error response for authentication failures.
     * Catches bad credentials exceptions and returns appropriate HTTP status.
     * 
     * @return HTTP 401 Unauthorized with error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleBadCredentialsException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials");
    }
}
