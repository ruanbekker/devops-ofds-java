# devops-ofds-java

DevOps Project - Online Food Delivery System

## Overview
The Online Food Delivery System (OFDS) is a microservices-based application designed for demonstrating DevOps practices. It showcases inter-service communication, API gateway routing, and integration of multiple services using Java Spring Boot, Spring Cloud Gateway, Docker, and Docker Compose.

## Architecture

The system consists of the following components:

1. API Gateway (Spring Cloud Gateway)
   - Acts as a single entry point for all requests.
   - Routes traffic to the appropriate backend service.
   - Supports load balancing and centralized routing.

2. Order Service (Spring Boot):
   - Manages customer orders.
   - Stores information such as customer name, dish, status, and restaurant ID.
   - Fetches restaurant information from the Restaurant Service when retrieving an order.

3. Restaurant Service (Spring Boot):
   - Manages restaurant data including name, location, and cuisine type.
   - Provides restaurant details based on restaurant ID.

4. Delivery Service (Spring Boot):
   - Manages delivery details, including order ID, driver name, and status.
   - Fetches order information from the **Order Service** when retrieving delivery details.

5. PostgreSQL Database:
   - Stores data for each service separately.
   - Uses Docker volumes for persistent storage.

## Flow of a Typical Request

1. The user makes an API request to the API Gateway.
2. The API Gateway routes the request to the appropriate service:
   - `/api/orders/**` -> Order Service
   - `/api/restaurants/**` -> Restaurant Service
   - `/api/deliveries/**` -> Delivery Service
3. The Order Service may call the Restaurant Service to fetch restaurant details when displaying an order.
4. The Delivery Service may call the Order Service to fetch order details when displaying a delivery.
5. All inter-service communication goes through the API Gateway for consistency.

## Endpoints

### Order Service

- Create Order:  

  ```bash
  curl -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" -d '{"customerName": "John", "dishName": "Pizza", "status": "Pending", "restaurantId": 1}'
  ```

- List Orders:  

  ```bash
  curl http://localhost:8080/api/orders
  ```

- Get Order by ID:  

  ```bash
  curl http://localhost:8080/api/orders/1
  ```

- Update Order Status:  

  ```bash
  curl -X PUT http://localhost:8080/api/orders/1/status -H "Content-Type: application/json" -d 'Delivered'
  ```

### Restaurant Service

- Create Restaurant:  

  ```bash
  curl -X POST http://localhost:8080/api/restaurants -H "Content-Type: application/json" -d '{"name": "Pizza Palace", "location": "Cape Town", "cuisineType": "Italian"}'
  ```

- List Restaurants:  

  ```bash
  curl http://localhost:8080/api/restaurants
  ```

- Get Restaurant by ID:  

  ```bash
  curl http://localhost:8080/api/restaurants/1
  ```

### Delivery Service

- Create Delivery:  

  ```bash
  curl -X POST http://localhost:8080/api/deliveries -H "Content-Type: application/json" -d '{"orderId": 1, "driverName": "Jane", "status": "In Transit"}'
  ```

- List Deliveries:  

  ```bash
  curl http://localhost:8080/api/deliveries
  ```

- Get Delivery by ID:  

  ```bash
  curl http://localhost:8080/api/deliveries/1
  ```

## Running the Application

To start all services, run:

```bash
docker-compose up --build
```

To stop all services, run:

```bash
docker-compose down -v
```

## Project Structure

```
services/
├── order-service/
├── restaurant-service/
├── delivery-service/
└── gateway/
```

## Architectural Diagram

Visual Representation of the Flow:

![](https://github.com/user-attachments/assets/8aa58a1a-c1f2-48b3-95d5-4718a07d409c)


Inter-Service Calls:

1. Order Service -> Restaurant Service:
   - API Gateway routes request from /api/orders/{id} to /api/restaurants/{restaurantId}.
   - Retrieves restaurant details when fetching an order.

2. Delivery Service -> Order Service:
   - API Gateway routes request from /api/deliveries/{id} to /api/orders/{orderId}.
   - Retrieves order details when fetching a delivery.

3. API Gateway:
   - Acts as a single entry point for all external and internal service communications.
   - Ensures that inter-service calls follow the same path as external calls.

## Explanation of Inter-Service Communication:

1. Order Service -> Restaurant Service:

   - When retrieving order details, the Order Service calls the Restaurant Service to get restaurant information.
   - The call goes through the API Gateway for consistency:

    ```bash
    http://gateway:8080/api/restaurants/{restaurantId}
    ```

2. Delivery Service -> Order Service:

   - When retrieving delivery details, the Delivery Service calls the Order Service to fetch order information.
   - The call goes through the API Gateway:

   ```bash
   http://gateway:8080/api/orders/{orderId}
   ```

3. API Gateway:

   - Handles both external and internal communication.
   - Provides a consistent access point to all services.

## Observability

### Traces

![Image](https://github.com/user-attachments/assets/fd1804a3-383b-4a9d-a81c-9c116420c014)
