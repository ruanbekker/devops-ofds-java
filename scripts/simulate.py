#!/usr/bin/env python3
import time
import random
import requests

RESTAURANTS = [
    {"name": "Pizza Palace", "location": "Cape Town", "cuisineType": "Italian"},
    {"name": "Taco Town", "location": "Cape Town", "cuisineType": "Mexican"},
    {"name": "Steak House", "location": "Cape Town", "cuisineType": "Grill"}
]

CUSTOMERS = ["John Doe", "Alice Smith", "Bob Johnson", "Maria Garcia", "James Lee"]
DISHES = ["Margherita Pizza", "Spicy Tacos", "Grilled Steak", "Veggie Wrap", "Sushi Platter"]
DRIVERS = ["Jane Doe", "Tom Driver", "Lara Speed", "Mike Wheels", "Sara Roadster"]

GATEWAY_URL = "http://localhost:8080"


def create_restaurants():
    print("Bootstrapping restaurants...")
    for r in RESTAURANTS:
        res = requests.post(f"{GATEWAY_URL}/api/restaurants", json=r)
        if res.status_code != 200:
            print("Failed to create restaurant:", res.text)


def create_order(customer_name, dish_name, restaurant_id):
    payload = {
        "customerName": customer_name,
        "dishName": dish_name,
        "status": "Pending",
        "restaurantId": restaurant_id
    }
    res = requests.post(f"{GATEWAY_URL}/api/orders", json=payload)
    if res.status_code != 200:
        print("Failed to create order:", res.text)
        return None
    return res.json().get("id")


def create_delivery(order_id, driver_name):
    payload = {
        "orderId": order_id,
        "driverName": driver_name,
        "status": "In Transit"
    }
    res = requests.post(f"{GATEWAY_URL}/api/deliveries", json=payload)
    if res.status_code != 200:
        print("Failed to create delivery:", res.text)
        return None
    return res.json().get("id")


def update_delivery_status(delivery_id):
    requests.put(f"{GATEWAY_URL}/api/deliveries/{delivery_id}/status", data="Delivered")


def update_order_status(order_id):
    requests.put(f"{GATEWAY_URL}/api/orders/{order_id}/status", data="Completed")


def simulate_traffic_loop():
    try:
        while True:
            customer = random.choice(CUSTOMERS)
            dish = random.choice(DISHES)
            restaurant_id = random.randint(1, len(RESTAURANTS))
            driver = random.choice(DRIVERS)

            print(f"📦 Creating order for {customer} - '{dish}' from restaurant #{restaurant_id}")
            order_id = create_order(customer, dish, restaurant_id)
            if not order_id:
                time.sleep(2)
                continue

            prep_delay = round(random.uniform(1.0, 4.0), 2)
            print(f"⏳ Waiting {prep_delay}s while order #{order_id} is being prepared...")
            time.sleep(prep_delay)

            print(f"🚗 Assigning delivery for order #{order_id} with driver {driver}")
            delivery_id = create_delivery(order_id, driver)
            if not delivery_id:
                continue

            delivery_delay = round(random.uniform(2.0, 5.0), 2)
            print(f"📍 Driver is en route, delivering order #{order_id} (ETA: {delivery_delay}s)...")
            time.sleep(delivery_delay)

            print(f"✅ Delivery #{delivery_id} completed for order #{order_id}")
            update_delivery_status(delivery_id)

            print(f"🧾 Marking order #{order_id} as completed")
            update_order_status(order_id)

            cooldown = round(random.uniform(1.0, 3.0), 2)
            print(f"✔️ Done with order #{order_id}. Taking a short break ({cooldown}s)...\n---\n")
            time.sleep(cooldown)

    except KeyboardInterrupt:
        print("🛑 Traffic simulation stopped by user.")


if __name__ == "__main__":
    create_restaurants()
    simulate_traffic_loop()

