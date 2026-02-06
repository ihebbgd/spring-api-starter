package com.codewithmosh.store.Services;

import com.codewithmosh.store.dtos.CheckoutRequest;
import com.codewithmosh.store.dtos.CheckoutResponse;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderItem;
import com.codewithmosh.store.entities.OrderStatus;
import com.codewithmosh.store.exceptions.CartIsEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.PaymentException;
import com.codewithmosh.store.exceptions.UserNotFoundException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;



    @Transactional
    public CheckoutResponse checkout(CheckoutRequest checkoutRequest) {
        var cart=cartRepository.getCartWithItems(checkoutRequest.getCartId()).orElse(null);
        if(cart==null){
            throw new CartNotFoundException();
        }
        if (cart.getCartItems().isEmpty()){
            throw new CartIsEmptyException();
        }

        var order= new Order(authService.getCurrentUser(),OrderStatus.PENDING,cart.getTotalPrice());
        cart.getCartItems().forEach(item->{
            var orderItem=new OrderItem(order,item.getProduct(), item.getQuantity());
            order.getOrderItems().add(orderItem);
        });
        orderRepository.save(order);

       try {
           //CREATE CHECKOUT SESSION
           var session=paymentGateway.createCheckoutSession(order);
           cartService.clearcart(cart.getId());

           return new CheckoutResponse(order.getId(),session.getCheckoutUrl());
       }catch (PaymentException e){
           orderRepository.delete(order);
           throw e;
       }


    }
}
