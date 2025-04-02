package com.ofds.order.controller;

import com.ofds.order.model.Order;
import com.ofds.order.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        order.setStatus("Pending");
        return orderRepository.save(order);
    }

    @GetMapping
    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}/status")
    public Order updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(status.replaceAll("\"", ""));  // Remove extra quotes from JSON
            return orderRepository.save(order);
        }).orElse(null);
    }
}

