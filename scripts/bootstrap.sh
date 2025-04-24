#!/usr/bin/env bash

# Create a restaurant
curl -X POST http://localhost:8080/api/restaurants \
  -H "Content-Type: application/json" \
  -d '{"name": "Pizza Palace", "location": "Cape Town", "cuisineType": "Italian"}'

# Create a new order at above restaurant
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName": "John Doe", "dishName": "Margherita Pizza", "status": "Pending", "restaurantId": 1}'

# Create a delivery which is linked to the above order
curl -X POST http://localhost:8080/api/deliveries \
  -H "Content-Type: application/json" \
  -d '{"orderId": 1, "driverName": "Jane Doe", "status": "In Transit"}'

# Update the delivery to delivered
curl -X PUT http://localhost:8080/api/deliveries/1/status \
  -H "Content-Type: application/json" \
  -d 'Delivered'

# Update the order status
curl -X PUT http://localhost:8080/api/orders/1/status \
  -H "Content-Type: application/json" \
  -d 'Delivered'
