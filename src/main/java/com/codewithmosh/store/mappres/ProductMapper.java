package com.codewithmosh.store.mappres;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "category_id", source = "category.id")
    ProductDto productToProductDto(Product product);
    Product productDtoToProduct(ProductDto productDto);
    @Mapping(target = "id", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductDto productDto);
}
