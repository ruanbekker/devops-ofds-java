package com.ofds.delivery.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Delivery {
    @Id
    @GeneratedValue
    private Long id;
    private Long orderId;
    private String driverName;
    private String status;
}

