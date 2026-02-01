package com.codewithmosh.store.mappres;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;

import com.codewithmosh.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "totalprice", expression = "java(cart.getTotalPrice())")
    CartDto cartToCartDto(Cart cart);

    @Mapping(target = "totalprice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto cartToCartDto(CartItem cartItem);
    Cart cartDtoToCart(CartDto cartDto);
}
