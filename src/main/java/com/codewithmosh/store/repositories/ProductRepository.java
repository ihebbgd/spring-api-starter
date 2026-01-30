package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategoryId(Byte categoryId);
    @EntityGraph(attributePaths = {"category"})
    @Query("SELECT p FROM Product p")
    List<Product> findAllWithCategory();

    @EntityGraph(attributePaths = {"category"})
    List<Product> findByCategory_Id(Byte categoryId);


//    @EntityGraph(attributePaths = {"category"})
//    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
//    List<Product> findWithCategory(@Param("id")Byte category_id);
}