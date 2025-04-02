package com.ofds.delivery.controller;

import com.ofds.delivery.model.Delivery;
import com.ofds.delivery.repository.DeliveryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;

    public DeliveryController(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
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

    @PutMapping("/{id}/status")
    public Delivery updateStatus(@PathVariable Long id, @RequestBody String status) {
        return deliveryRepository.findById(id).map(delivery -> {
            delivery.setStatus(status);
            return deliveryRepository.save(delivery);
        }).orElse(null);
    }
}

