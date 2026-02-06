package com.codewithmosh.store.controllers;

import com.codewithmosh.store.Services.OrderService;
import com.codewithmosh.store.dtos.OrderDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getAllOrders(){
        return orderService.getAllOrders();


    }

    @GetMapping("/{orderId}")
    public OrderDto getOrderById(@PathVariable("orderId") Long orderId){
        return orderService.getOrder(orderId);

    }
}
