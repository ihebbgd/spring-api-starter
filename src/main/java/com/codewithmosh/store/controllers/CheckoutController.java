package com.codewithmosh.store.controllers;

import com.codewithmosh.store.Services.CheckoutService;
import com.codewithmosh.store.Services.WebhookRequest;
import com.codewithmosh.store.dtos.CheckoutRequest;
import com.codewithmosh.store.dtos.CheckoutResponse;
import com.codewithmosh.store.repositories.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;




    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest checkoutRequest) {
        return ResponseEntity.ok(checkoutService.checkout(checkoutRequest));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestHeader Map<String, String> headers,
                                              @RequestBody String payload) {
        checkoutService.handleWebhookEvent(new WebhookRequest(payload, headers));
        return ResponseEntity.ok().build();

    }

}