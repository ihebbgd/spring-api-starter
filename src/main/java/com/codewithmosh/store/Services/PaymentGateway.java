package com.codewithmosh.store.Services;

import com.codewithmosh.store.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
}
