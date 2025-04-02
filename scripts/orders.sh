#!/usr/bin/env bash

# create new order linked to the restaurant
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName": "John Doe", "dishName": "Margherita Pizza", "status": "Pending", "restaurantId": 1}'

# list all orders
curl http://localhost:8080/api/orders

# get order details with restaurant info
curl http://localhost:8080/api/orders/1

# update order status
curl -X PUT http://localhost:8080/api/orders/1/status \
  -H "Content-Type: application/json" \
  -d '"Delivered"'

# delete an order
curl -X DELETE http://localhost:8080/api/orders/1


