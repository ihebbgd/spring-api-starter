package com.codewithmosh.store.filters;

import com.codewithmosh.store.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter for processing JWT tokens in HTTP requests.
 * 
 * Use Cases:
 * - Extract JWT tokens from Authorization headers
 * - Validate token authenticity and expiration
 * - Set user authentication in security context
 * - Enable stateless authentication for protected endpoints
 * 
 * This filter runs once per request and processes JWT tokens before
 * the request reaches the controller layer.
 */
@AllArgsConstructor
@Component
public class JwtAuthentificationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    /**
     * Main filter method that processes JWT authentication for each request.
     * 
     * Use Case: Intercepts all HTTP requests to validate JWT tokens
     * and set up authentication context for protected endpoints.
     * 
     * Process:
     * 1. Extract Authorization header
     * 2. Check for Bearer token format
     * 3. Validate token integrity and expiration
     * 4. Extract user ID and set authentication context
     * 5. Continue filter chain or block request
     * 
     * @param request HTTP request object
     * @param response HTTP response object
     * @param filterChain Filter chain for request processing
     * @throws ServletException If servlet processing fails
     * @throws IOException If I/O operations fail
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader=request.getHeader("Authorization");
        if(authHeader==null || !authHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request,response);
            return;
        }
        var token=authHeader.replace("Bearer ","");
        if (!jwtService.validateToken(token))
        {
            filterChain.doFilter(request,response);
            return;
        }
        var authentication=new UsernamePasswordAuthenticationToken(
                jwtService.getuserIdToken(token),null,null
        );
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request,response);
    }
}
