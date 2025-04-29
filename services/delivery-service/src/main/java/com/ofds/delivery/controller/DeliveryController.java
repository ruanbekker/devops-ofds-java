package com.ofds.delivery.controller;

import com.ofds.delivery.model.Delivery;
import com.ofds.delivery.service.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private static final Logger log = LoggerFactory.getLogger(DeliveryController.class);

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public Delivery createDelivery(@RequestBody Delivery delivery) {
        log.info("Creating delivery");
        return deliveryService.createDelivery(delivery);
    }

    @GetMapping
    public List<Delivery> listDeliveries() {
        log.info("Listing deliveries");
        return deliveryService.listDeliveries();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getDeliveryWithOrder(@PathVariable Long id) {
        log.info("Fetching delivery with order for ID {}", id);
        return deliveryService.getDeliveryWithOrder(id).orElse(null);
    }

    @PutMapping("/{id}/status")
    public Delivery updateStatus(@PathVariable Long id, @RequestBody String status) {
        log.info("Updating status for delivery ID {}", id);
        return deliveryService.updateStatus(id, status).orElse(null);
    }
}

