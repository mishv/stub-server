package com.example.controller;

import com.example.model.OrderInfo;
import com.example.service.DataStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class StubController {
    private final DataStorageService storage;
    private final Random rand = new Random();

    public StubController(DataStorageService storage) {
        this.storage = storage;
    }

    private void delay() {
        try {
            long delay = 450 + rand.nextInt(101); // 450–550 мс
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {}
    }

    @GetMapping("/orderId/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrder(@PathVariable String orderId) {
        delay();
        OrderInfo info = storage.getOrCreateOrder(orderId);
        return ResponseEntity.ok(Map.of(
                "orderId", info.getOrderId(),
                "cargoId", info.getCargoId(),
                "price", info.getPrice()
        ));
    }

    @GetMapping("/cargo/{cargoId}")
    public ResponseEntity<?> getCargo(@PathVariable String cargoId) {
        delay();
        OrderInfo info = storage.getByCargoId(cargoId);
        if (info == null) return ResponseEntity.status(404).body("not found");

        return ResponseEntity.ok(Map.of(
                "cargoId", info.getCargoId(),
                "orderId", info.getOrderId(),
                "status", info.getNextStatus()
        ));
    }

    @PostMapping("/price/{orderId}")
    public ResponseEntity<?> updatePrice(@PathVariable String orderId, @RequestBody Map<String, Integer> body) {
        delay();
        OrderInfo info = storage.getByOrderId(orderId);
        if (info == null) return ResponseEntity.status(404).body("not found");

        int newPrice = body.getOrDefault("price", info.getPrice());
        info.setPrice(newPrice);
        return ResponseEntity.ok(Map.of(
                "orderId", info.getOrderId(),
                "price", info.getPrice()
        ));
    }
}

