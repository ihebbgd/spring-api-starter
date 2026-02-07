package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "created_at",insertable = false,updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    public Order(User customer, PaymentStatus status, BigDecimal totalPrice) {
        this.customer = customer;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}