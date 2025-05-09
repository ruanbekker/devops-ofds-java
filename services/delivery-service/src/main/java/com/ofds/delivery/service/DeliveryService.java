package com.ofds.delivery.service;

import com.ofds.delivery.model.Delivery;
import com.ofds.delivery.repository.DeliveryRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeliveryService {
    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);

    private final DeliveryRepository deliveryRepository;
    private final RestTemplate restTemplate;
    private final Counter deliveriesCompletedCounter;
    private final Counter deliveriesCreatedCounter;

    public DeliveryService(DeliveryRepository deliveryRepository, RestTemplate restTemplate, MeterRegistry meterRegistry) {
        this.deliveryRepository = deliveryRepository;
        this.restTemplate = restTemplate;
        this.deliveriesCompletedCounter = meterRegistry.counter("deliveries_completed_total");
        this.deliveriesCreatedCounter = meterRegistry.counter("deliveries_created_total");
    }

    public Delivery createDelivery(Delivery delivery) {
        delivery.setStatus("Pending");
        deliveriesCreatedCounter.increment();
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
            String cleanStatus = status.replaceAll("\"", "").trim();
            delivery.setStatus(cleanStatus);
	    log.info("[debugger] Updating status to '{}'", cleanStatus);
            if ("Delivered".equalsIgnoreCase(cleanStatus)) {
		log.info("[debugger] Incrementing deliveries_completed_total counter");
                deliveriesCompletedCounter.increment();
            }
            return deliveryRepository.save(delivery);
        });
    }
}

