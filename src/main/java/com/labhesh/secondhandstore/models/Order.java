package com.labhesh.secondhandstore.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.labhesh.secondhandstore.enums.OrderStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "\"order\"")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    private LocalDateTime orderDate;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
    private Double shippingCost;
    private String shippingAddress;
    private Double tax;
    private Double totalAmount;

    @Builder.Default
    private String status = OrderStatus.PENDING.name(); // e.g., "PENDING", "SHIPPED", "DELIVERED"

    // getters and setters
}
