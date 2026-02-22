package com.bookstore.controllers;

import com.bookstore.DTOs.OrderDTO;
import com.bookstore.DTOs.OrderDetailsDTO;
import com.bookstore.DTOs.PagedResponseDTO;
import com.bookstore.DTOs.requests.PlaceOrderRequest;
import com.bookstore.DTOs.requests.UpdateOrderStatusRequest;
import com.bookstore.enums.OrderStatus;
import com.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
@Tag(name = "Orders", description = "Operations related to orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService= orderService;
    }

    /*
    -------------------User------------------
     */

    @GetMapping("/users/me/orders")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get my orders", description = "Returns a list of order summaries for the authenticated user. Access = [USER]")
    public ResponseEntity<List<OrderDTO>> getMyOrders(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return ResponseEntity.ok(orderService.getOrdersForCurrentUser(username));
    }

    @GetMapping("/users/me/orders/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get my order's details by id", description = "Returns an order's full details for the authenticated user. Access = [USER]")
    public ResponseEntity<OrderDetailsDTO> getMyOrderDetails(@PathVariable Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return ResponseEntity.ok(orderService.getOrderDetailsForCurrentUser(username,id));
    }

    @PostMapping("/users/me/orders")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create a new order", description = "Returns the created order for the authenticated user. Access = [USER]")
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody PlaceOrderRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return ResponseEntity.ok(orderService.createNewOrder(request,username));
    }

    /*
    -------------------Admin------------------
     */

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders", description = "Returns a list of order summaries of all users. Access = [ADMIN]")
    public ResponseEntity<PagedResponseDTO<OrderDTO>> getAllOrders(@RequestParam(required = false) String username,
                                                         @RequestParam(required = false) OrderStatus status,
                                                         @RequestParam (defaultValue = "0") int page,
                                                         @RequestParam (defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(orderService.getAllOrders(Optional.ofNullable(username),Optional.ofNullable(status), pageable));
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get an order by id", description = "Returns full details of a user's order. Access = [ADMIN]"
    )
    public ResponseEntity<OrderDetailsDTO> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/orders/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status by id", description = "Returns the order summary with the updated status. Access = [USER]"
    )
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusRequest request){
        return ResponseEntity.ok(orderService.updateOrderStatusByOrderId(id,request));
    }
}
