package com.ofds.restaurant.controller;

import com.ofds.restaurant.model.Restaurant;
import com.ofds.restaurant.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
	log.info("Adding restaurant");
        return restaurantRepository.save(restaurant);
    }

    @GetMapping
    public List<Restaurant> listRestaurants() {
	log.info("Listing restaurants");
        return restaurantRepository.findAll();
    }

    @GetMapping("/{id}")
    public Restaurant getRestaurant(@PathVariable Long id) {
	log.info("Getting restaurant with ID {}", id);
        return restaurantRepository.findById(id).orElse(null);
    }
}

