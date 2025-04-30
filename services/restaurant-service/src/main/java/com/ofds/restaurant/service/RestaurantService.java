package com.ofds.restaurant.service;

import com.ofds.restaurant.model.Restaurant;
import com.ofds.restaurant.repository.RestaurantRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final Counter restaurantCreatedCounter;

    public RestaurantService(RestaurantRepository restaurantRepository, MeterRegistry meterRegistry) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantCreatedCounter = meterRegistry.counter("restaurants_created_total");
    }

    public Restaurant addRestaurant(Restaurant restaurant) {
        restaurantCreatedCounter.increment();
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> listRestaurants() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> getRestaurant(Long id) {
        return restaurantRepository.findById(id);
    }
}

