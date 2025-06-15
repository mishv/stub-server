package com.example.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderInfo {
    private final String orderId;
    private final String cargoId;
    private int price;
    private final AtomicInteger statusIndex = new AtomicInteger(0);
    private static final List<String> statuses = List.of("NEW", "In Process", "In Process", "Delivered", "Done");

    public OrderInfo(String orderId, String cargoId, int price) {
        this.orderId = orderId;
        this.cargoId = cargoId;
        this.price = price;
    }

    public String getOrderId() { return orderId; }
    public String getCargoId() { return cargoId; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getNextStatus() {
        int idx = statusIndex.getAndUpdate(i -> (i + 1) % statuses.size());
        return statuses.get(idx);
    }
}

