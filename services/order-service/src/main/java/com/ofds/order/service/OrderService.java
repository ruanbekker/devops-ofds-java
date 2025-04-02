package com.ofds.order.service;

import com.ofds.order.model.Order;
import com.ofds.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        order.setStatus("Pending");
        return orderRepository.save(order);
    }
}

