package com.codewithmosh.store.config;

import com.codewithmosh.store.filters.JwtAuthentificationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security Configuration class for JWT-based authentication.
 * 
 * Use Cases:
 * - Configure security filter chain for API endpoints
 * - Set up JWT authentication filter
 * - Define password encoding strategy
 * - Configure authentication provider and manager
 * - Define public vs protected endpoints
 * 
 * This configuration enables stateless JWT authentication for the REST API.
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final JwtAuthentificationFilter jwtAuthentificationFilter;

    /**
     * Configures password encoder for secure password hashing.
     * 
     * Use Case: Encrypts user passwords before storing in database.
     * BCrypt is industry standard with built-in salt generation.
     * 
     * @return BCryptPasswordEncoder instance for password hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    /**
     * Configures authentication provider for user authentication.
     * 
     * Use Case: Bridges UserDetailsService with password encoder
     * to authenticate users against database credentials.
     * 
     * @param userDetailsService Service for loading user data
     * @return Configured DaoAuthenticationProvider instance
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Exposes authentication manager for authentication processing.
     * 
     * Use Case: Used by AuthController to authenticate user credentials
     * during login process.
     * 
     * @param authenticationConfiguration Spring's authentication configuration
     * @return AuthenticationManager bean for credential validation
     * @throws Exception If configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



    /**
     * Configures the main security filter chain for HTTP requests.
     * 
     * Use Cases:
     * - Disables CSRF protection (not needed for stateless JWT APIs)
     * - Sets session management to stateless (no server-side sessions)
     * - Defines public endpoints (login, register, carts)
     * - Requires authentication for all other endpoints
     * - Adds JWT filter before username/password authentication
     * 
     * @param httpSecurity HTTP security configuration builder
     * @param authenticationProvider Authentication provider for credential validation
     * @return Configured SecurityFilterChain
     * @throws Exception If security configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity, AuthenticationProvider authenticationProvider) throws Exception {
        httpSecurity.sessionManagement(c->
                    c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c->
                        c.requestMatchers("/carts/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthentificationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
