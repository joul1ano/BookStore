package com.bookstore.service;

import com.bookstore.DTOs.BookDTO;
import com.bookstore.DTOs.OrderDTO;
import com.bookstore.DTOs.OrderDetailsDTO;
import com.bookstore.DTOs.PagedResponseDTO;
import com.bookstore.DTOs.requests.PlaceOrderRequest;
import com.bookstore.DTOs.requests.UpdateOrderStatusRequest;
import com.bookstore.enums.OrderStatus;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.mappers.OrderItemMapper;
import com.bookstore.mappers.OrderMapper;
import com.bookstore.model.*;
import com.bookstore.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartItemsRepository cartItemsRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartService cartService;


    public OrderService(OrderRepository orderRepository,
                        OrderItemsRepository orderItemsRepository,
                        UserRepository userRepository,
                        ShoppingCartRepository cartRepository,
                        ShoppingCartItemsRepository cartItemsRepository,
                        OrderMapper orderMapper,
                        OrderItemMapper orderItemMapper,
                        ShoppingCartService cartService){
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartService = cartService;
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
        updateOtherCarts(orderItems);

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
    public PagedResponseDTO<OrderDTO> getAllOrders(Optional<String> username, Optional<OrderStatus> status, Pageable pageable){
        Page<Order> page;
        if (username.isPresent() && status.isPresent()) {
            page = orderRepository.findAllByUser_UsernameAndStatus(username.get(), status.get(), pageable);
        } else if (username.isPresent() && status.isEmpty()) {
            page = orderRepository.findAllByUser_Username(username.get(),pageable);
        } else if (username.isEmpty() && status.isPresent()) {
            page = orderRepository.findAllByStatus(status.get(), pageable);
        }else {
            page = orderRepository.findAll(pageable);
        }

        List<OrderDTO> orders = page.getContent().stream().map(orderMapper::toDTO).toList();
        PagedResponseDTO<OrderDTO> ordersPage = PagedResponseDTO.<OrderDTO>builder()
                .content(orders)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();

        return ordersPage;
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

    /*
    When a user places an order, the order items availabilities are updated. So after many orders, if a book's availability
    is for example set to 5 , and some user has that book in his shopping cart with a quantity of 6, the book quantity in his
    cart and all the other carts that contain that book (and that the quantity is exceeding the availability), should be
    reduced to equal availability. In this case, the book's quantity in this shopping cart will be set to 5.
     */
    private void updateOtherCarts(List<OrderItem> orderItems){
        for(OrderItem item : orderItems){
            // Find all cart items (across all users) for this book where the requested quantity exceeds the available stock
            List<ShoppingCartItem> cartItemsThatContainBook = cartItemsRepository
                    .findAllByBook_IdAndQuantityGreaterThan(item.getBook().getId(),item.getBook().getAvailability());

            for(ShoppingCartItem cartItem : cartItemsThatContainBook){
                User user = userRepository.findByShoppingCart_Id(cartItem.getShoppingCart().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                cartService.updateItemQuantity(user.getId(), cartItem.getBook().getId(),cartItem.getBook().getAvailability());
            }
        }

    }

}
