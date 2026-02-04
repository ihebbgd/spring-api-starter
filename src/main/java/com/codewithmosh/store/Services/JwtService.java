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

    public boolean validateToken(String token){
        try {
            var claims = getClaims(token);

            return claims.getExpiration().after(new Date());


        }
        catch (JwtException e){
            return false;}

    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getuserIdToken(String token){
        return Long.valueOf(getClaims(token).getSubject());

    }




}
