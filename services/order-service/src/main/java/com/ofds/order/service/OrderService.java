package com.ofds.order.service;

import com.ofds.order.model.Order;
import com.ofds.order.repository.OrderRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final Counter orderCreatedCounter;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate, MeterRegistry meterRegistry) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.orderCreatedCounter = meterRegistry.counter("orders_created_total");
    }

    public Order createOrder(Map<String, Object> request) {
        Order order = new Order();
        order.setCustomerName((String) request.get("customerName"));
        order.setDishName((String) request.get("dishName"));
        order.setStatus((String) request.get("status"));
        order.setRestaurantId(Long.parseLong(request.get("restaurantId").toString()));
        orderCreatedCounter.increment();
        return orderRepository.save(order);
    }

    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    public Optional<Map<String, Object>> getOrderWithRestaurant(Long id) {
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
        });
    }

    public Optional<Order> updateOrderStatus(Long id, String status) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(status.replaceAll("\"", ""));
            return orderRepository.save(order);
        });
    }
}

