package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.ChangePasswordRequest;
import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.mappres.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterUserRequest request, UriComponentsBuilder builder) {
        var user = userMapper.toEntity(request);
        userRepository.save(user);
        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body("Email already exists");
        }

        var location = builder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body(userMapper.userToUserDto(user));
    }

@PutMapping("/{id}")
public ResponseEntity<UserDto> updateUser(@PathVariable(name="id") Long id, @RequestBody UpdateUserRequest request){
        var user = userRepository.findById(id).orElse(null);
        if (user==null){
            return ResponseEntity.notFound().build();
        }
        userMapper.updateUserFromRequest(request, user);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.userToUserDto(user));
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable(name="id") Long id){
        var user=userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();

}

@PostMapping("/{id}/change-password")
public ResponseEntity<Void> changePassword(@PathVariable(name="id") Long id, @RequestBody ChangePasswordRequest request){
    var user=userRepository.findById(id).orElse(null);
    if(user==null){
        return ResponseEntity.notFound().build();
    }
    if(!user.getPassword().equals(request.getOldPassword())){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    userMapper.updateUserPassword(request, user);
    userRepository.save(user);
    return ResponseEntity.noContent().build();

}




}
