package com.codewithmosh.store.Services;

import com.codewithmosh.store.dtos.OrderDto;
import com.codewithmosh.store.exceptions.CantAccessThisOrderException;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.mappres.OrderMapper;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders(){
        var user=authService.getCurrentUser();
        var orders= orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();


    }

    public OrderDto getOrder(Long orderId) {
        var order = orderRepository.getOrderWithitems(orderId).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        var user = authService.getCurrentUser();
        if (!order.getCustomer().getId().equals(user.getId())) {
            throw new CantAccessThisOrderException();
        }

        return orderMapper.toDto(order);

    }







}
