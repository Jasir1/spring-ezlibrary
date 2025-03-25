package com.mhdjasir.ezLibrary.domain.order;

import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@SecurityRequirement(name = "ezLibrary")
public class OrderResource {

    private final OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<ApplicationResponseDTO> addOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.addOrder(orderDTO));
    }

    //common orders
    @PostMapping("/cancel/{order-id}") // for admin
    public ResponseEntity<ApplicationResponseDTO> cancelOrder(@PathVariable("order-id") Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @GetMapping("/get/{id}") // for admin
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
    @GetMapping("/get") // for admin
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    @GetMapping("/get/ongoing") // for admin
    public ResponseEntity<List<Order>> ongoingOrders() {
        return ResponseEntity.ok(orderService.ongoingOrders());
    }

    @GetMapping("/get/pending") // for admin
    public ResponseEntity<List<Order>> pendingOrders() {
        return ResponseEntity.ok(orderService.pendingOrders());
    }

    @GetMapping("/get/cancelled") // for admin
    public ResponseEntity<List<Order>> canceledOrders() {
        return ResponseEntity.ok(orderService.canceledOrders());
    }

    @PutMapping("/update/{order-id}") // for admin
    public ResponseEntity<ApplicationResponseDTO> updateOrderStatus(@PathVariable("order-id") Long orderId, @RequestBody String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    //own orders
    @PostMapping("/cancel/own/{order-id}") // for own order
    public ResponseEntity<ApplicationResponseDTO> cancelOwnOrder(@PathVariable("order-id") Long orderId) {
        return ResponseEntity.ok(orderService.cancelOwnOrder(orderId));
    }
    @GetMapping("/get/own")
    public ResponseEntity<List<Order>> getAllOwnOrders() {
        return ResponseEntity.ok(orderService.getAllOwnOrders());
    }

    @GetMapping("/get/own/{id}")
    public ResponseEntity<Order> getOwnOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getOwnOrderById(id));
    }
    @GetMapping("/get/own/cancelled")
    public ResponseEntity<List<Order>> canceledOwnOrders() {
        return ResponseEntity.ok(orderService.canceledOwnOrders());
    }
    @GetMapping("/get/own/ongoing")
    public ResponseEntity<List<Order>> ongoingOwnOrders() {
        return ResponseEntity.ok(orderService.ongoingOwnOrders());
    }

    @GetMapping("/get/own/pending")
    public ResponseEntity<List<Order>> pendingOwnOrders() {
        return ResponseEntity.ok(orderService.pendingOwnOrders());
    }


}
