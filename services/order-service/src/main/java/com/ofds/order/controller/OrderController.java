package com.ofds.order.controller;

import com.ofds.order.model.Order;
import com.ofds.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderController(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public Order createOrder(@RequestBody Map<String, Object> request) {
	log.info("Creating order");

        Order order = new Order();
        order.setCustomerName((String) request.get("customerName"));
        order.setDishName((String) request.get("dishName"));
        order.setStatus((String) request.get("status"));
        order.setRestaurantId(Long.parseLong(request.get("restaurantId").toString()));
        return orderRepository.save(order);
    }

    @GetMapping
    public List<Order> listOrders() {
	log.info("Listing orders");
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getOrderWithRestaurant(@PathVariable Long id) {
	log.info("Fetching order with ID {}", id);

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
        log.info("Updating order status for ID {}", id);
	return orderRepository.findById(id).map(order -> {
            order.setStatus(status.replaceAll("\"", ""));
            return orderRepository.save(order);
        }).orElse(null);
    }
}

