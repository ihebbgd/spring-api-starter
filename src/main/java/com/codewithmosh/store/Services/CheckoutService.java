package com.codewithmosh.store.Services;

import com.codewithmosh.store.dtos.CheckoutRequest;
import com.codewithmosh.store.dtos.CheckoutResponse;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderItem;
import com.codewithmosh.store.entities.PaymentStatus;
import com.codewithmosh.store.exceptions.CartIsEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.PaymentException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

        var order= new Order(authService.getCurrentUser(), PaymentStatus.PENDING,cart.getTotalPrice());
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

    public void handleWebhookEvent(WebhookRequest request){
        paymentGateway.parseWebhookEvent(request)
                .ifPresent(paymentResult -> {
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentstatus());
                    orderRepository.save(order);

                });


    }





}
