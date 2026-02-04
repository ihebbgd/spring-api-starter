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

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService JwtService;
    private  final UserRepository userRepository;
    private final UserMapper userMapper;

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
    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader){
        var token =authHeader.replace("Bearer ","");
        return JwtService.validateToken(token);
    }

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





    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleBadCredentialsException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials");
    }
}
