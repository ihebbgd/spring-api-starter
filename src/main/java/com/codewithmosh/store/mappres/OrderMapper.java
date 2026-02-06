package com.codewithmosh.store.mappres;

import com.codewithmosh.store.dtos.OrderDto;
import com.codewithmosh.store.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "orderItems", target = "items")
    OrderDto toDto(Order order);




}

