package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.Additemtocart;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.dtos.UpdateCartItem;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.mappres.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
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
public class CartController {
    private final CartRepository cartRepository;
    private final  CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder builder){
        Cart cart=new Cart();
        cart.setId(UUID.randomUUID());
        cartRepository.save(cart);
        var location=builder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();
        return ResponseEntity.created(location).body(cartMapper.cartToCartDto(cart));

    }
    @PostMapping("/{id}/items")
    public ResponseEntity<CartItemDto> updateCart(@PathVariable UUID id, @RequestBody Additemtocart request){
        var cart=cartRepository.getCartWithItems(id).orElse(null);
        if(cart==null){
            return ResponseEntity.notFound().build();
        }
        var product=productRepository.findById(request.getProductId()).orElse(null);
        if(product==null){
            return ResponseEntity.badRequest().build();
        }
        var cartItem=cart.addItem(product);
        cartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartMapper.cartToCartDto(cartItem));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID id){
        var cart=cartRepository.getCartWithItems(id).orElse(null);
        if(cart==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartMapper.cartToCartDto(cart));
    }
    @PutMapping("/{id}/items/{productId}")
    public ResponseEntity<?> updateItem(@PathVariable UUID id, @PathVariable Long productId,@Valid @RequestBody UpdateCartItem request){
        var cart=cartRepository.getCartWithItems(id).orElse(null);
        if(cart==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Cart not found"));
        }
        var cartItem=cart.getItem(productId);
        if(cartItem==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Cart item not found"));
        }
        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        return ResponseEntity.ok(cartMapper.cartToCartDto(cartItem));
    }



}
