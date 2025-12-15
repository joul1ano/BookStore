package com.bookstore.controllers;

import com.bookstore.DTOs.AdminOrderItemDTO;
import com.bookstore.DTOs.OrderDTO;
import com.bookstore.DTOs.OrderDetailsDTO;
import com.bookstore.DTOs.requests.PlaceOrderRequest;
import com.bookstore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService= orderService;
    }

    @GetMapping("/users/me/orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderDTO>> getMyOrders(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return ResponseEntity.ok(orderService.getOrdersForCurrentUser(username));
    }

    @GetMapping("/users/me/orders/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDetailsDTO> getMyOrderDetails(@PathVariable Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return ResponseEntity.ok(orderService.getOrderDetailsForCurrentUser(username,id));

    }

    @PostMapping("/users/me/orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody PlaceOrderRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return ResponseEntity.ok(orderService.createNewOrder(request,username));
    }
}
