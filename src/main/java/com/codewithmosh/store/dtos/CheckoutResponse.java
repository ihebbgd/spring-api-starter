package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CheckoutResponse {
    private Long orderId;
    private String checkoutUrl;

    public CheckoutResponse(Long id, String checkoutUrl) {
        this.orderId=id;
        this.checkoutUrl=checkoutUrl;


    }
}
