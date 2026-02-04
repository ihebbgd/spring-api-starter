package com.codewithmosh.store.controllers;

import com.codewithmosh.store.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleProductNotFoundException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Product not found"));
    }
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCategoryNotFoundException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Category not found"));
    }
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCartNotFoundException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Cart not found"));
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleUserNotFoundException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","User not found"));
    }
    @ExceptionHandler(EmailAlreadyExists.class)
    public ResponseEntity<Map<String,String>> handleEmailAlreadyExists(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Email already exists"));
    }
    @ExceptionHandler(FalsePassword.class)
    public ResponseEntity<Map<String,String>> handleFalsePassword(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","False password"));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        var errors=new HashMap<String,String>();
        exception.getBindingResult().getFieldErrors().forEach(error->errors.put(error.getField(),error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
    }

}
