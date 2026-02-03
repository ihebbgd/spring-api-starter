package com.codewithmosh.store.controllers;

import com.codewithmosh.store.Services.CartService;
import com.codewithmosh.store.dtos.Additemtocart;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.dtos.UpdateCartItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Cart")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder builder){
        var cartDto= cartService.createCart();
        var location=builder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(location).body(cartDto);

    }
    @PostMapping("/{id}/items")
    @Operation(summary = "Add item to cart")
    public ResponseEntity<CartItemDto> updateCart(@Parameter(description = "Cart id") @PathVariable UUID id, @RequestBody Additemtocart request){
        var cartItemDto=cartService.addItemToCart(id,request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID id){
        var cartDto=cartService.getcart(id);
        return ResponseEntity.ok(cartDto);
    }



    @PutMapping("/{id}/items/{productId}")
    public ResponseEntity<?> updateItem(@PathVariable UUID id, @PathVariable Long productId,@Valid @RequestBody UpdateCartItem request){
        var cartItemDto=cartService.updateitem(id,productId,request.getQuantity());

        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{id}/items/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable UUID id, @PathVariable Long productId){
        cartService.removeitem(id,productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID id){
        cartService.clearcart(id);
        return ResponseEntity.noContent().build();
    }












}
