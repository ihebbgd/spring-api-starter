package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @Column(name = "id", nullable = false,updatable = false,columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    @Column(name = "date_Created", nullable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    @PrePersist
    private void prePersist(){
        this.dateCreated=LocalDate.now();
    }
    public BigDecimal getTotalPrice(){
        return this.cartItems.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
    }
    public CartItem getItem(Long productId){
        return this.cartItems.stream().filter(ci->ci.getProduct().getId().equals(productId)).findFirst().orElse(null);
    }


    public CartItem addItem(Product product){
        var cartItem=getItem(product.getId());
        if(cartItem!=null){
            cartItem.setQuantity(cartItem.getQuantity()+1);
        }
        else{
            cartItem = new CartItem();
            cartItem.setCart(this);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItems.add(cartItem);
        }
        return cartItem;

    }

}