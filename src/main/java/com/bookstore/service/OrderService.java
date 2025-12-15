package com.bookstore.service;

import com.bookstore.DTOs.requests.PlaceOrderRequest;
import com.bookstore.enums.OrderStatus;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.model.*;
import com.bookstore.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartItemsRepository cartItemsRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemsRepository orderItemsRepository,
                        UserRepository userRepository,
                        ShoppingCartRepository cartRepository,
                        ShoppingCartItemsRepository cartItemsRepository){
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    @Transactional
    public void createNewOrder(PlaceOrderRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + request.getUserId() + " not found"));
        ShoppingCart cart = cartRepository.findByUserId(request.getUserId());

        Order order = Order.builder()
                .id(null)
                .user(user)
                .datePlaced(LocalDateTime.now())
                .shippingAddress(request.getShippingAddress())
                .totalCost(0) //TODO
                .paymentMethod(request.getPaymentMethod())
                .userNotes(request.getUserNotes())
                .status(OrderStatus.PENDING)
                .statusLastUpdated(LocalDateTime.now())
                .orderItems(List.of()) //TODO
                .build();
    }

    private List<OrderItem> createOrderItems(ShoppingCart cart, Order order){
        List<OrderItem> orderItems =
                cartItemsRepository.findAllByShoppingCart_Id(cart.getId())
                        .stream()
                        .map(cartItem -> OrderItem.builder()
                                .order(order)
                                .book(cartItem.getBook())
                                .quantity(cartItem.getQuantity())
                                .totalCost(cartItem.getBook().getPrice() * cartItem.getQuantity())
                                .build()
                        )
                        .toList();

        orderItemsRepository.saveAll(orderItems);

        return orderItems;
    }
}
