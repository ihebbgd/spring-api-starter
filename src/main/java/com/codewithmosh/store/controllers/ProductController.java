package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.mappres.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productrepository;
    private final ProductMapper productmapper;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam(required = false,defaultValue = "",name= "category_id") Byte category_id){
        if(category_id!=null){
            return productrepository.findByCategory_Id(category_id).stream().map(productmapper::productToProductDto).toList();
        }
        return productrepository.findAllWithCategory().stream().map(productmapper::productToProductDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
        var product= productrepository.findById(id).orElse(null);
        if(product==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productmapper.productToProductDto(product));
    }
}
