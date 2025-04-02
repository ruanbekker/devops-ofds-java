package com.ofds.order.controller;

import com.ofds.order.model.Order;
import com.ofds.order.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.restTemplate = new RestTemplate();
    }

    @PostMapping
    public Order createOrder(@RequestBody Map<String, Object> request) {
        Order order = new Order();
        order.setCustomerName((String) request.get("customerName"));
        order.setDishName((String) request.get("dishName"));
        order.setStatus((String) request.get("status"));
        order.setRestaurantId(Long.parseLong(request.get("restaurantId").toString())); // Link to restaurant
        return orderRepository.save(order);
    }

    @GetMapping
    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getOrderWithRestaurant(@PathVariable Long id) {
        return orderRepository.findById(id).map(order -> {
            String restaurantUrl = "http://gateway:8080/api/restaurants/" + order.getRestaurantId();
            Map<String, Object> restaurant = restTemplate.getForObject(restaurantUrl, Map.class);

            return Map.of(
                    "id", order.getId(),
                    "customerName", order.getCustomerName(),
                    "dishName", order.getDishName(),
                    "status", order.getStatus(),
                    "restaurant", restaurant
            );
        }).orElse(null);
    }

    @PutMapping("/{id}/status")
    public Order updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(status.replaceAll("\"", ""));
            return orderRepository.save(order);
        }).orElse(null);
    }
}

