package com.codewithmosh.store.controllers;


import com.codewithmosh.store.Services.AuthService;
import com.codewithmosh.store.Services.JwtService;
import com.codewithmosh.store.config.JwtConfig;
import com.codewithmosh.store.dtos.JwtResponse;
import com.codewithmosh.store.dtos.LoginRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.mappres.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    private final JwtConfig JwtConfig;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                             HttpServletResponse response){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        var user=userRepository.findByEmail(loginRequest.getEmail());
        var accesstoken=JwtService.generateAcessToken(user);
        var refreshToken=JwtService.generateRefreshToken(user);

        var cookie=new Cookie("refreshToken",refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(JwtConfig.getRefreshTokenExpiration()); //7 days
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accesstoken.toString()));
    }
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken
    ){
        var jwt=JwtService.parseToken(refreshToken);
        if(jwt==null || ! jwt.isValid()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();}

        var user=userRepository.findById(jwt.getUserId()).orElse(null);
        if(user==null)
            return ResponseEntity.badRequest().build();

        var accesstoken=JwtService.generateAcessToken(user);

        return ResponseEntity.ok(new JwtResponse(accesstoken.toString()));

    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        var user=authService.getCurrentUser();

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
