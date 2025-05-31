# 🍕 devops-ofds-java

**DevOps Project – Online Food Delivery System**

---

## 🧭 Overview

The **Online Food Delivery System (OFDS)** is a microservices-based application demonstrating DevOps best practices. It showcases:

* Inter-service communication
* API Gateway routing
* Observability with tracing, logging, metrics, and profiling

Built using **Java Spring Boot**, **Spring Cloud Gateway**, **Docker**, and **Docker Compose**.

---

## 🏗️ Architecture

### 🔧 Components

1. **API Gateway (Spring Cloud Gateway)**

   * Entry point for all requests
   * Routes traffic to backend services
   * Supports load balancing and centralized routing

2. **Order Service**

   * Manages customer orders
   * Stores customer name, dish, status, and restaurant ID
   * Retrieves restaurant details from the Restaurant Service

3. **Restaurant Service**

   * Manages restaurant data: name, location, cuisine type
   * Returns details for a given restaurant ID

4. **Delivery Service**

   * Manages deliveries with order ID, driver name, and status
   * Retrieves order details from the Order Service

5. **PostgreSQL**

   * Dedicated database per service
   * Uses Docker volumes for persistence

---

## 🔁 Request Flow

1. Client sends request to **API Gateway**
2. Gateway routes based on URI:

   * `/api/orders/**` → Order Service
   * `/api/restaurants/**` → Restaurant Service
   * `/api/deliveries/**` → Delivery Service
3. **Order Service** may query **Restaurant Service**
4. **Delivery Service** may query **Order Service**
5. All inter-service calls go through the Gateway

---

## 📡 API Endpoints

### 🧾 Order Service

```bash
# Create Order
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName": "John", "dishName": "Pizza", "status": "Pending", "restaurantId": 1}'

# List Orders
curl http://localhost:8080/api/orders

# Get Order by ID
curl http://localhost:8080/api/orders/1

# Update Order Status
curl -X PUT http://localhost:8080/api/orders/1/status \
  -H "Content-Type: application/json" \
  -d 'Delivered'
```

### 🍽️ Restaurant Service

```bash
# Create Restaurant
curl -X POST http://localhost:8080/api/restaurants \
  -H "Content-Type: application/json" \
  -d '{"name": "Pizza Palace", "location": "Cape Town", "cuisineType": "Italian"}'

# List Restaurants
curl http://localhost:8080/api/restaurants

# Get Restaurant by ID
curl http://localhost:8080/api/restaurants/1
```

### 🚚 Delivery Service

```bash
# Create Delivery
curl -X POST http://localhost:8080/api/deliveries \
  -H "Content-Type: application/json" \
  -d '{"orderId": 1, "driverName": "Jane", "status": "In Transit"}'

# List Deliveries
curl http://localhost:8080/api/deliveries

# Get Delivery by ID
curl http://localhost:8080/api/deliveries/1
```

---

## ▶️ Running the App

Start all services:

```bash
docker-compose up --build
```

Stop all services:

```bash
docker-compose down -v
```

---

## 📁 Project Structure

```
services/
├── gateway/
├── order-service/
├── restaurant-service/
└── delivery-service/
```

---

## 🗺️ System Diagram

**Architecture Overview:**

![Diagram](https://github.com/user-attachments/assets/8aa58a1a-c1f2-48b3-95d5-4718a07d409c)

### 🔄 Inter-Service Communication

| From             | To                 | Description                                               |
| ---------------- | ------------------ | --------------------------------------------------------- |
| Order Service    | Restaurant Service | Fetches restaurant info when returning an order           |
| Delivery Service | Order Service      | Fetches order info when returning a delivery              |
| All              | API Gateway        | Routes requests internally and externally for consistency |

Example:

```bash
http://gateway:8080/api/restaurants/{restaurantId}
http://gateway:8080/api/orders/{orderId}
```

---

## 🔍 Observability

Run a complete observability stack to monitor logs, metrics, traces, and profiling:

### 🚀 Bootstrap Demo Traffic

```bash
./scripts/simulate.py
```

### 🧰 Stack Overview

| Service        | Description                                                                                                  |
| -------------- | ------------------------------------------------------------------------------------------------------------ |
| **Grafana**    | Visualization layer for metrics, logs, traces, and profiling. [http://localhost:3000](http://localhost:3000) |
| **Prometheus** | Scrapes and stores time-series metrics from services                                                         |
| **Loki**       | Centralized log aggregation                                                                                  |
| **Promtail**   | Ships container logs to Loki                                                                                 |
| **Tempo**      | Collects and stores distributed traces                                                                       |
| **Pyroscope**  | Continuous performance profiling (CPU, memory)                                                               |

### 📊 Dashboard Highlights

* Real-time metrics (e.g., request count, order status)
* Structured logging with search
* Distributed traces via Tempo
* CPU/memory flamegraphs with Pyroscope

### 🔍 Viewing Traces

* Go to **Grafana > Explore**
* Select **Tempo** as data source
* Filter by service name (e.g., `api-gateway`) and span name (`GET delivery-service`)
* View traces and spans

![Tempo Trace](https://github.com/user-attachments/assets/fd1804a3-383b-4a9d-a81c-9c116420c014)

### 📈 Dashboards

Under **Dashboards > Demo Dashboard**, you’ll find:

* HTTP request breakdown
* Orders created over time
* Deliveries created and completed

![Grafana Dashboard](https://github.com/user-attachments/assets/eb52d6c3-0f5e-494a-bba8-45a26875a2a7)


