package com.codewithmosh.store.controllers;

import com.codewithmosh.store.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
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
    @ExceptionHandler(CartIsEmptyException.class)
    public ResponseEntity<Map<String,String>> handleCartIsEmptyException() {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Cart is empty"));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,String>> handleunreadablemessage(){
        return ResponseEntity.badRequest().body(Map.of("error","Invalid request body"));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleOrderNotFoundException(){
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Order not found"));
    }
    @ExceptionHandler(CantAccessThisOrderException.class)
    public ResponseEntity<Map<String, String>> handleException(){
        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","You don't have access to this order"));
    }
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Map<String, String>> handlePaymentException(){
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","create checkout session failed"));
    }

}
