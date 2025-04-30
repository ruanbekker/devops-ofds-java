package com.ofds.order.controller;

import com.ofds.order.model.Order;
import com.ofds.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody Map<String, Object> request) {
        log.info("Creating order");
        return orderService.createOrder(request);
    }

    @GetMapping
    public List<Order> listOrders() {
        log.info("Listing orders");
        return orderService.listOrders();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getOrderWithRestaurant(@PathVariable Long id) {
        log.info("Fetching order with ID {}", id);
        return orderService.getOrderWithRestaurant(id).orElse(null);
    }

    @PutMapping("/{id}/status")
    public Order updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        log.info("Updating order status for ID {}", id);
        return orderService.updateOrderStatus(id, status).orElse(null);
    }
}

