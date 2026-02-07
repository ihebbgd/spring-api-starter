package com.codewithmosh.store.Services;

import com.codewithmosh.store.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResult {
    private Long orderId;
    private PaymentStatus paymentstatus;
}
