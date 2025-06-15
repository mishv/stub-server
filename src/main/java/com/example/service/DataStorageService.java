package com.example.service;

import com.example.model.OrderInfo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@Service
public class DataStorageService {
    private final Cache<String, OrderInfo> orderCache;

    public DataStorageService() {
        this.orderCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofHours(3))
                .maximumSize(100_000)
                .build();
    }

    public synchronized OrderInfo getOrCreateOrder(String orderId) {
        return orderCache.get(orderId, key -> {
            String cargoId = UUID.randomUUID().toString();
            int price = new Random().nextInt(900) + 100;
            return new OrderInfo(orderId, cargoId, price);
        });
    }

    public OrderInfo getByOrderId(String orderId) {
        return orderCache.getIfPresent(orderId);
    }

    public OrderInfo getByCargoId(String cargoId) {
        return orderCache.asMap().values().stream()
                .filter(o -> o.getCargoId().equals(cargoId))
                .findFirst().orElse(null);
    }
}

