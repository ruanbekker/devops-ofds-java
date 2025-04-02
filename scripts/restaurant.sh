#!/usr/bin/env bash

# create new restaurant
curl -X POST http://localhost:8080/api/restaurants \
  -H "Content-Type: application/json" \
  -d '{"name": "Pizza Palace", "location": "Cape Town", "cuisineType": "Italian"}'


#list all restaurants
curl http://localhost:8080/api/restaurants


# get a restaurant by id
curl http://localhost:8080/api/restaurants/1


