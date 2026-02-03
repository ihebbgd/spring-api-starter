package com.codewithmosh.store.Services;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.exceptions.CategoryNotFoundException;
import com.codewithmosh.store.controllers.GlobalExceptionHandler;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappres.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productrepository;
    private final ProductMapper productmapper;
    private final CategoryRepository categoryrepository;
    private final GlobalExceptionHandler globalExceptionHandler;

    public List<ProductDto> getallproducts(Byte category_id){
        if(category_id!=null){
            return productrepository.findByCategory_Id(category_id).stream().map(productmapper::productToProductDto).toList();
        }
        return productrepository.findAllWithCategory().stream().map(productmapper::productToProductDto).toList();
    }
    public ProductDto getproductbyid(Long id){
        var product= productrepository.findById(id).orElse(null);
        if(product==null){
            throw new ProductNotFoundException();
        }
        return productmapper.productToProductDto(product);

    }

    public ProductDto createproduct(ProductDto productDto){
        var category=categoryrepository.findById(productDto.getCategory_id()).orElse(null);
        if(category==null){
            throw new ProductNotFoundException();
        }
        var product=productmapper.productDtoToProduct(productDto);
        product.setCategory(category);
        product.setId(product.getId());
        productrepository.save(product);
        return productmapper.productToProductDto(product);
    }

    public ProductDto updateproductbyid(Long id,ProductDto productDto){
        var Product =productrepository.findById(id).orElse(null);
        if(Product==null){
            throw new ProductNotFoundException();
        }
        var category=categoryrepository.findById(productDto.getCategory_id()).orElse(null);
        if(category==null){
            throw new CategoryNotFoundException();
        }
        productmapper.updateProduct(Product, productDto);
        Product.setCategory(category);
        productrepository.save(Product);
        return productmapper.productToProductDto(Product);
    }

    public void deleteproductbyid(Long id){

        var product=productrepository.findById(id).orElse(null);
        if(product==null){
            throw new ProductNotFoundException();
        }
        productrepository.delete(product);
    }





}
