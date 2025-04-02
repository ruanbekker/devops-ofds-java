#!/usr/bin/env bash

# create new delivery linked to an order
curl -X POST http://localhost:8080/api/deliveries \
  -H "Content-Type: application/json" \
  -d '{"orderId": 1, "driverName": "Jane Doe", "status": "In Transit"}'


# list all deliveries
curl http://localhost:8080/api/deliveries

# update delivery status
curl -X PUT http://localhost:8080/api/deliveries/1/status \
  -H "Content-Type: application/json" \
  -d '"Delivered"'

# get delivery details with order info
curl http://localhost:8080/api/deliveries/1


