package com.codewithmosh.store.Services;

import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.repositories.UserRepository;
import io.jsonwebtoken.impl.security.EdwardsCurve;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User getCurrentUser(){
        var authentification= SecurityContextHolder.getContext().getAuthentication();
        var userId=(Long)authentification.getPrincipal();

        return userRepository.findById(userId).orElse(null);
    }
}
