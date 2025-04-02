package com.ofds.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ofds.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

