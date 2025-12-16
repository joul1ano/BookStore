package com.bookstore.service;

import com.bookstore.DTOs.OrderDTO;
import com.bookstore.DTOs.OrderDetailsDTO;
import com.bookstore.DTOs.requests.PlaceOrderRequest;
import com.bookstore.DTOs.requests.UpdateOrderStatusRequest;
import com.bookstore.enums.OrderStatus;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.OrderItemMapper;
import com.bookstore.mappers.OrderMapper;
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
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;


    public OrderService(OrderRepository orderRepository,
                        OrderItemsRepository orderItemsRepository,
                        UserRepository userRepository,
                        ShoppingCartRepository cartRepository,
                        ShoppingCartItemsRepository cartItemsRepository,
                        OrderMapper orderMapper,
                        OrderItemMapper orderItemMapper){
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }
    /*
    --------------User------------------
     */

    @Transactional
    public OrderDTO createNewOrder(PlaceOrderRequest request, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow();
        ShoppingCart cart = cartRepository.findByUserId(user.getId());

        Order order = Order.builder()
                .id(null)
                .user(user)
                .datePlaced(LocalDateTime.now())
                .shippingAddress(request.getShippingAddress())
                .paymentMethod(request.getPaymentMethod())
                .userNotes(request.getUserNotes())
                .status(OrderStatus.PENDING)
                .statusLastUpdated(LocalDateTime.now())
                .build();
        List<OrderItem> orderItems = createOrderItems(cart,order);
        double totalCost = orderItems.stream().mapToDouble(OrderItem::getTotalCost).sum();

        order.setOrderItems(orderItems);
        order.setTotalCost(totalCost);

        //Updating user's total amount spent
        order.getUser().setTotalAmountSpent(order.getUser().getTotalAmountSpent() + order.getTotalCost());
        //Updating book availabilities
        updateBookAvailability(order);

        Order savedOrder = orderRepository.save(order);

        emptyCart(cart);

        return orderMapper.toDTO(savedOrder);
    }

    public List<OrderDTO> getOrdersForCurrentUser(String username){
        User user = userRepository.findByUsername(username).orElseThrow();

        return orderRepository.findAllByUser_Id(user.getId()).stream().map(orderMapper::toDTO).toList();
    }

    public OrderDetailsDTO getOrderDetailsForCurrentUser(String username, Long orderId){
        User user = userRepository.findByUsername(username).orElseThrow();
        Order order = orderRepository.findById(orderId).orElseThrow(() ->new ResourceNotFoundException("Order not found"));
        List<OrderItem> orderItems = orderItemsRepository.findAllByOrder_Id(orderId);

        return OrderDetailsDTO.builder()
                .orderId(order.getId())
                .userId(user.getId())
                .datePlaced(order.getDatePlaced())
                .shippingAddress(order.getShippingAddress())
                .totalCost(order.getTotalCost())
                .paymentMethod(order.getPaymentMethod())
                .userNotes(order.getUserNotes())
                .status(order.getStatus())
                .statusLastUpdated(order.getStatusLastUpdated())
                .items(orderItems.stream().map(orderItemMapper::toOrderItemDTO).toList())
                .build();
    }

    /*
    --------------Admin------------------
     */
    public List<OrderDTO> getAllOrders(){
        return orderRepository.findAll().stream().map(orderMapper::toDTO).toList();
    }

    public OrderDetailsDTO getOrderById(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        OrderDetailsDTO orderDetailsDTO = OrderDetailsDTO.builder()
                .orderId(orderId)
                .userId(order.getUser().getId())
                .datePlaced(order.getDatePlaced())
                .shippingAddress(order.getShippingAddress())
                .totalCost(order.getTotalCost())
                .paymentMethod(order.getPaymentMethod())
                .userNotes(order.getUserNotes())
                .status(order.getStatus())
                .statusLastUpdated(order.getStatusLastUpdated())
                .items(order.getOrderItems().stream().map(orderItemMapper::toOrderItemDTO).toList())
                .build();

        return orderDetailsDTO;
    }

    public OrderDTO updateOrderStatusByOrderId(Long orderId, UpdateOrderStatusRequest request){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(request.getStatus());
        order.setStatusLastUpdated(LocalDateTime.now());

        return orderMapper.toDTO(orderRepository.save(order));
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

        return orderItems;
    }

    private void emptyCart(ShoppingCart cart){
        cart.setItemCount(0);
        cart.setTotalCost(0);
        cart.setLastUpdatedAt(LocalDateTime.now());
        cart.getShoppingCartItems().clear();

        cartRepository.save(cart);
    }

    private void updateBookAvailability(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            Book book = item.getBook();

            int newAvailability = book.getAvailability() - item.getQuantity();

            book.setAvailability(Math.max(newAvailability, 0));
        }
    }

}
