package com.ofds.delivery.service;

import com.ofds.delivery.model.Delivery;
import com.ofds.delivery.repository.DeliveryRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final RestTemplate restTemplate;
    private final Counter deliveriesCompletedCounter;

    public DeliveryService(DeliveryRepository deliveryRepository, RestTemplate restTemplate, MeterRegistry meterRegistry) {
        this.deliveryRepository = deliveryRepository;
        this.restTemplate = restTemplate;
        this.deliveriesCompletedCounter = meterRegistry.counter("deliveries_completed_total");
    }

    public Delivery createDelivery(Delivery delivery) {
        delivery.setStatus("Pending");
        return deliveryRepository.save(delivery);
    }

    public List<Delivery> listDeliveries() {
        return deliveryRepository.findAll();
    }

    public Optional<Map<String, Object>> getDeliveryWithOrder(Long id) {
        return deliveryRepository.findById(id).map(delivery -> {
            String orderUrl = "http://gateway:8080/api/orders/" + delivery.getOrderId();
            Map<String, Object> order = restTemplate.getForObject(orderUrl, Map.class);

            return Map.of(
                "id", delivery.getId(),
                "driverName", delivery.getDriverName(),
                "status", delivery.getStatus(),
                "order", order
            );
        });
    }

    public Optional<Delivery> updateStatus(Long id, String status) {
        return deliveryRepository.findById(id).map(delivery -> {
            delivery.setStatus(status.replaceAll("\"", "")); // clean up JSON quotes
            if ("Completed".equalsIgnoreCase(delivery.getStatus())) {
                deliveriesCompletedCounter.increment(); // Increment counter on completed deliveries
            }
            return deliveryRepository.save(delivery);
        });
    }
}

