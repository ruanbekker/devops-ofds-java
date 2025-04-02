#!/usr/bin/env bash

# create new order
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName": "John Doe", "dishName": "Margherita Pizza", "status": "Pending"}'

# list all orders
curl http://localhost:8080/api/orders

# get a specific order by id
curl http://localhost:8080/api/orders/1

# update order status
curl -X PUT http://localhost:8080/api/orders/1/status \
  -H "Content-Type: application/json" \
  -d '"Delivered"'

# delete an order
curl -X DELETE http://localhost:8080/api/orders/1


