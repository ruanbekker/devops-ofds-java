#!/usr/bin/env bash

# Create a restaurant
echo "1: Creating Restaurant"
curl -X POST http://localhost:8080/api/restaurants \
  -H "Content-Type: application/json" \
  -d '{"name": "Pizza Palace", "location": "Cape Town", "cuisineType": "Italian"}' ; echo

# Create a new order at above restaurant
echo "2: Creating Order from the Restaurant"
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName": "John Doe", "dishName": "Margherita Pizza", "status": "Pending", "restaurantId": 1}' ; echo

# Create a delivery which is linked to the above order
echo "3: Creating a Delivery from the Order"
curl -X POST http://localhost:8080/api/deliveries \
  -H "Content-Type: application/json" \
  -d '{"orderId": 1, "driverName": "Jane Doe", "status": "In Transit"}' ; echo

# Update the delivery to delivered
echo "4: Updating the Delivery Status to Delivered"
curl -X PUT http://localhost:8080/api/deliveries/1/status \
  -H "Content-Type: application/json" \
  -d 'Delivered' ; echo

# Update the order status
echo "5: Update the Order to Completed"
curl -X PUT http://localhost:8080/api/orders/1/status \
  -H "Content-Type: application/json" \
  -d 'Completed' ; echo

# Verify Status
echo "6: Verify Delivery Status"
curl http://localhost:8080/api/deliveries/1 ; echo

echo "7: Verify Order Status"
curl http://localhost:8080/api/orders/1 ; echo
