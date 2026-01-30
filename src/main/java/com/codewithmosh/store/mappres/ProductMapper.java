package com.codewithmosh.store.mappres;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category_id", source = "category.id")
    ProductDto productToProductDto(Product product);
}
