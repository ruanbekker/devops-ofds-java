package com.ofds.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "customer_order")
@Data
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private Long restaurantId;
    private String customerName;
    private String dishName;
    private String status;
}

