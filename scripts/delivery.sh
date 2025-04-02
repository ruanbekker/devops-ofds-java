#!/usr/bin/env bash

# create new delivery
curl -X POST http://localhost:8080/api/deliveries \
  -H "Content-Type: application/json" \
  -d '{"orderId": 1, "driverName": "John Doe", "status": "Pending"}'


# list all deliveries
curl http://localhost:8080/api/deliveries

# update delivery status
curl -X PUT http://localhost:8080/api/deliveries/1/status \
  -H "Content-Type: application/json" \
  -d '"Delivered"'

# get specific delivery by id
curl http://localhost:8080/api/deliveries/1


