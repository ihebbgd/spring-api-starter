package com.codewithmosh.store.Services;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappres.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@AllArgsConstructor

public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartDto createCart(){
        var cart=new Cart();
        cart.setId(UUID.randomUUID());
        cartRepository.save(cart);
        return cartMapper.cartToCartDto(cart);
    }
    public CartItemDto addItemToCart(UUID id, Long productId){
        var cart=cartRepository.getCartWithItems(id).orElse(null);
        if(cart==null){
            throw new CartNotFoundException();
        }
        var product=productRepository.findById(productId).orElse(null);
        if(product==null){
            throw new ProductNotFoundException();
        }
        var cartItem=cart.addItem(product);
        cartRepository.save(cart);
        return cartMapper.cartToCartDto(cartItem);
    }

    public CartDto getcart(UUID id){
        var cart=cartRepository.getCartWithItems(id).orElse(null);
        if(cart==null){
            throw new CartNotFoundException();
        }
        return cartMapper.cartToCartDto(cart);
    }
    public CartDto updateitem(UUID id, Long productId, Integer quantity){
        var cart=cartRepository.getCartWithItems(id).orElse(null);
        if(cart==null){
            throw new CartNotFoundException();
        }
        var cartItem=cart.getItem(productId);
        if(cartItem==null){
            throw new ProductNotFoundException();
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.cartToCartDto(cartItem.getCart());
    }
    public void removeitem(UUID id, Long productId){
        var cart=cartRepository.getCartWithItems(id).orElse(null);
        if(cart==null){
            throw new CartNotFoundException();
        }
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearcart(UUID id){
        var cart=cartRepository.getCartWithItems(id).orElse(null);
        if(cart==null){
            throw new CartNotFoundException();
        }
        cart.clear();
        cartRepository.save(cart);

    }


}
