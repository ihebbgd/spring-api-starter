package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.mappres.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productrepository;
    private final ProductMapper productmapper;
    private final CategoryRepository categoryrepository;

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

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, UriComponentsBuilder builder){
        var category=categoryrepository.findById(productDto.getCategory_id()).orElse(null);
        var location=builder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        if(category==null){
            return ResponseEntity.badRequest().build();
        }
        var product=productmapper.productDtoToProduct(productDto);
        product.setCategory(category);
        product.setId(product.getId());
        productrepository.save(product);
        return ResponseEntity.created(location).body(productmapper.productToProductDto(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProductById(@PathVariable Long id, @RequestBody ProductDto productDto){
        var Product =productrepository.findById(id).orElse(null);
        if(Product==null){
            return ResponseEntity.notFound().build();
        }
        var category=categoryrepository.findById(productDto.getCategory_id()).orElse(null);
        if(category==null){
            return ResponseEntity.badRequest().build();
        }
        productmapper.updateProduct(Product, productDto);
        Product.setCategory(category);
        productrepository.save(Product);
        return ResponseEntity.ok(productmapper.productToProductDto(Product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id){
        var product=productrepository.findById(id).orElse(null);
        if(product==null){
            return ResponseEntity.notFound().build();
        }
        productrepository.delete(product);
        return ResponseEntity.ok().build();
    }


}
