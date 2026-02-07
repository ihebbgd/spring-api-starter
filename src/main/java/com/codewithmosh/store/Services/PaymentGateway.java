package com.codewithmosh.store.Services;

import com.codewithmosh.store.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookEvent(WebhookRequest request);
}
