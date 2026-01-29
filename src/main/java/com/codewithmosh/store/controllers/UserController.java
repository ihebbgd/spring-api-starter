package com.codewithmosh.store.controllers;


import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class UserController {
    private final UserRepository userRepository;


    @GetMapping("/users")
    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }




    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        var user = userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
