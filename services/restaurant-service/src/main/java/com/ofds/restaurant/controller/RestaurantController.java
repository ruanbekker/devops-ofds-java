package com.ofds.restaurant.controller;

import com.ofds.restaurant.model.Restaurant;
import com.ofds.restaurant.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        log.info("Adding restaurant");
        return restaurantService.addRestaurant(restaurant);
    }

    @GetMapping
    public List<Restaurant> listRestaurants() {
        log.info("Listing restaurants");
        return restaurantService.listRestaurants();
    }

    @GetMapping("/{id}")
    public Restaurant getRestaurant(@PathVariable Long id) {
        log.info("Getting restaurant with ID {}", id);
        return restaurantService.getRestaurant(id).orElse(null);
    }
}

