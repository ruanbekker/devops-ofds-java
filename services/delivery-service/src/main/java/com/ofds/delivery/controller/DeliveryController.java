package com.ofds.delivery.controller;

import com.ofds.delivery.model.Delivery;
import com.ofds.delivery.repository.DeliveryRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;
    private final RestTemplate restTemplate;

    public DeliveryController(DeliveryRepository deliveryRepository, RestTemplate restTemplate) {
        this.deliveryRepository = deliveryRepository;
        this.restTemplate = restTemplate;
    }


    @PostMapping
    public Delivery createDelivery(@RequestBody Delivery delivery) {
        delivery.setStatus("Pending");
        return deliveryRepository.save(delivery);
    }

    @GetMapping
    public List<Delivery> listDeliveries() {
        return deliveryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getDeliveryWithOrder(@PathVariable Long id) {
        return deliveryRepository.findById(id).map(delivery -> {
            String orderUrl = "http://gateway:8080/api/orders/" + delivery.getOrderId();
            Map<String, Object> order = restTemplate.getForObject(orderUrl, Map.class);

            return Map.of(
                "id", delivery.getId(),
                "driverName", delivery.getDriverName(),
                "status", delivery.getStatus(),
                "order", order
            );
        }).orElse(null);
    }

    @PutMapping("/{id}/status")
    public Delivery updateStatus(@PathVariable Long id, @RequestBody String status) {
        return deliveryRepository.findById(id).map(delivery -> {
            delivery.setStatus(status);
            return deliveryRepository.save(delivery);
        }).orElse(null);
    }
}

