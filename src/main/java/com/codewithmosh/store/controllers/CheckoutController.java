package com.codewithmosh.store.controllers;

import com.codewithmosh.store.Services.CheckoutService;
import com.codewithmosh.store.dtos.CheckoutRequest;
import com.codewithmosh.store.dtos.CheckoutResponse;
import com.codewithmosh.store.entities.OrderStatus;
import com.codewithmosh.store.repositories.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;

    @Value("${stripe.webhookSecret}")
    private String stripeWebhookSecret;

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest checkoutRequest) {
        return ResponseEntity.ok(checkoutService.checkout(checkoutRequest));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestHeader("Stripe-Signature") String signature,
                                              @RequestBody String payload) {
        try {
            var event = Webhook.constructEvent(payload, signature, stripeWebhookSecret);
            var dataObjectDeserializer = event.getDataObjectDeserializer();

            // Initialize stripeObject
            com.stripe.model.StripeObject stripeObject = null;

            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();
            } else {
                // This throws EventDataObjectDeserializationException, so we must catch it below
                stripeObject = dataObjectDeserializer.deserializeUnsafe();
            }

            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    if (stripeObject instanceof PaymentIntent paymentIntent) {
                        var orderId = paymentIntent.getMetadata().get("order_id");

                        if (orderId != null) {
                            var order = orderRepository.findById(Long.valueOf(orderId)).orElse(null);
                            if (order != null) {
                                order.setStatus(OrderStatus.PAID);
                                orderRepository.save(order);
                                System.out.println("Order paid: " + order.getId());
                            }
                        }
                    }
                }
                case "payment_intent.payment_failed" -> {
                    // Handle failure
                }
            }
            return ResponseEntity.ok().build();

        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        } catch (com.stripe.exception.EventDataObjectDeserializationException e) {
            // Handle the deserialization error
            System.out.println("Deserialization failed: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}