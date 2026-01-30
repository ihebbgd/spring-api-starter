package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.mappres.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getAllUsers(@RequestParam(required = false,defaultValue = "",name= "sorts") String sortBy){
        Set<String> allowedSorts = Set.of("name", "email");
        if(!allowedSorts.contains(sortBy)){
            sortBy = "name";
        }
        return userRepository.findAll(Sort.by(sortBy).descending()).stream().map(userMapper::userToUserDto).toList();
    }




    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        var user = userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.userToUserDto(user));
    }
}
