package com.codewithmosh.store.controllers;

import com.codewithmosh.store.Services.ProductService;
import com.codewithmosh.store.dtos.ProductDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Tag(name = "Product")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam(required = false,defaultValue = "",name= "category_id") Byte category_id){
        return productService.getallproducts(category_id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getproductbyid(id));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, UriComponentsBuilder builder){
        var product=productService.createproduct(productDto);
        var location=builder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(location).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProductById(@PathVariable Long id, @RequestBody ProductDto productDto){
        var Product =productService.updateproductbyid(id,productDto);
        return ResponseEntity.ok(Product);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id){
        productService.deleteproductbyid(id);
        return ResponseEntity.ok().build();
    }







}
