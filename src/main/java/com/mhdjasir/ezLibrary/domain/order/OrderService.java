package com.mhdjasir.ezLibrary.domain.order;

import com.mhdjasir.ezLibrary.domain.book.Book;
import com.mhdjasir.ezLibrary.domain.book.BookRepository;
import com.mhdjasir.ezLibrary.domain.cart.CartRepository;
import com.mhdjasir.ezLibrary.domain.security.entity.User;
import com.mhdjasir.ezLibrary.domain.security.entity.UserRole;
import com.mhdjasir.ezLibrary.domain.security.repos.UserRepository;
import com.mhdjasir.ezLibrary.domain.user.UserService;
import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import com.mhdjasir.ezLibrary.exception.ApplicationCustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ShippingInfoRepository shippingInfoRepository;
    private final OrderItemsRepository orderItemsRepository;

    @Transactional
    public ApplicationResponseDTO addOrder(OrderDTO orderDTO) {
        User currentUser = userRepository.findById(userService.getCurrentUser().getId())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        // Create and set ShippingInfo
        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setAddress(orderDTO.getAddress());
        shippingInfo.setCountry(orderDTO.getCountry());
        shippingInfo.setCity(orderDTO.getCity());
        shippingInfo.setPhoneNo(orderDTO.getPhoneNo());
        shippingInfo.setPostalCode(orderDTO.getPostalCode());

        // Create and set Order
        Order order = new Order();
        order.setUserId(currentUser.getId());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingInfo(shippingInfo); // Link ShippingInfo to Order
        shippingInfo.setOrder(order); // Link Order to ShippingInfo (bidirectional relationship)

        List<OrderItems> orderItemsList = new ArrayList<>();
        double totalPrice = 0.0;

        // Process each item in the OrderDTO
        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            // Validate Book
            Book book = bookRepository.findById(itemDTO.getBookId())
                    .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "BOOK_NOT_FOUND", "Book not found"));

            // Calculate item price
            double itemTotalPrice = itemDTO.getQty() * book.getPrice();
            totalPrice += itemTotalPrice;

            // Create OrderItems
            OrderItems orderItem = new OrderItems();
            orderItem.setBookId(book.getId());
            orderItem.setBook(book);
            orderItem.setQty(itemDTO.getQty());
            orderItem.setPrice(itemTotalPrice);
            orderItem.setOrder(order); // Link OrderItems to Order
            orderItemsList.add(orderItem);
        }

        // Set calculated fields for Order
        order.setOrderItems(orderItemsList); // Link OrderItems to Order
        order.setTotalPrice(totalPrice);
        order.setShippingPrice(280.0); // Example fixed shipping price
        order.setPaidAt(LocalDateTime.now()); // Assuming the order is paid immediately
        order.setDeliveredAt(null); // Initially null, will be set upon delivery

        // Save Order (cascades to ShippingInfo and OrderItems because of CascadeType.ALL)
        orderRepository.save(order);

        // Return success response
        return new ApplicationResponseDTO(HttpStatus.CREATED, "ORDER_ADDED_SUCCESSFULLY", "Order added successfully!");
    }


    public ApplicationResponseDTO cancelOrder(Long orderId) {
//        boolean isAtLeastOneBookFound = false;
//        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
//                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));
//
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_NOT_FOUND", "Order not found"));
//
//        List<Long> bookIdsInOrder = order.getOrderItems().stream()
//                .map(OrderItems::getBookId)
//                .toList();
//
//        for (Long bookId : bookIdsInOrder) {
//            boolean bookExists = bookRepository.findByIdAndUserIdAndDelete(bookId, userService.getCurrentUser().getId(), false).isPresent();
//
//            if (bookExists) {
//                isAtLeastOneBookFound = true;
//                break;
//            }
//        }
//
//        if (isAtLeastOneBookFound) {
//            if (orderRepository.findByIdAndUserIdAndStatus(orderId, user.getId(), OrderStatus.CANCELLED).isPresent()) {
//                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_ALREADY_CANCELLED", "Order already cancelled!");
//            }
//            if (orderRepository.findByIdAndUserIdAndStatus(orderId, user.getId(), OrderStatus.PENDING).isEmpty()) {
//                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_IS_PROCESSING_CANNOT_CANCEL_ORDER", "Order is processing. Cannot cancel the order!");
//            }
//
//            order.setStatus(OrderStatus.CANCELLED);
//            orderRepository.save(order);
//            return new ApplicationResponseDTO(HttpStatus.OK, "ORDER_CANCELED_SUCCESSFULLY", "Order cancelled successfully!");
//        } else {
//            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "BOOK_NOT_FOUND", "No matching books found!");
//        }
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        if (user.getUserRole() == UserRole.ADMIN) {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_NOT_FOUND", "Order not found"));

            if (orderRepository.findByIdAndStatus(orderId, OrderStatus.CANCELLED).isPresent()) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_ALREADY_CANCELLED", "Order already cancelled!");
            }
            if (orderRepository.findByIdAndStatus(orderId, OrderStatus.PENDING).isEmpty()) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_IS_PROCESSING_CANNOT_CANCEL_ORDER", "Order is processing. Cannot cancel the order!");
            }

            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            return new ApplicationResponseDTO(HttpStatus.OK, "ORDER_CANCELED_SUCCESSFULLY", "Order cancelled successfully!");
        } else {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_NOT_FOUND", "Order not found");
        }
    }

    public ApplicationResponseDTO cancelOwnOrder(Long orderId) {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        Order order = orderRepository.findByIdAndUserId(orderId, userService.getCurrentUser().getId())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_NOT_FOUND", "Order not found"));

        if (orderRepository.findByIdAndUserIdAndStatus(orderId, user.getId(), OrderStatus.CANCELLED).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_ALREADY_CANCELLED", "Order already cancelled!");
        }
        if (orderRepository.findByIdAndUserIdAndStatus(orderId, user.getId(), OrderStatus.PENDING).isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_IS_PROCESSING_CANNOT_CANCEL_ORDER", "Order is processing. Cannot cancel the order!");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return new ApplicationResponseDTO(HttpStatus.OK, "ORDER_CANCELED_SUCCESSFULLY", "Order cancelled successfully!");
    }


    public Order getOrderById(Long orderId) {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        if (user.getUserRole() == UserRole.ADMIN) {
            return orderRepository.findById(orderId).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "ORDER_ID_NOT_FOUND", "Order id not found"));

        } else {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_NOT_FOUND", "Order not found");
        }
    }

    public List<Order> getAllOrders() {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        if (user.getUserRole() == UserRole.ADMIN) {
            List<Order> orderList = orderRepository.findAll();
            if (orderList.isEmpty()) {
                throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_ORDER_FOUND", "No order found");
            }
            return orderList;

        } else {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "RESOURCE_NOT_FOUND", "Resource not found");
        }
    }

    public List<Order> getAllOwnOrders() {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        List<Order> orderList = orderRepository.findAllByUserId(user.getId());
        if (orderList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_ORDER_FOUND", "No order found");
        }
        return orderList;
    }

    public Order getOwnOrderById(Long id) {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

            return orderRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "ORDER_ID_NOT_FOUND", "Order id not found"));

    }

    public List<Order> ongoingOrders() {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        if (user.getUserRole() == UserRole.ADMIN) {
            return orderRepository.findByStatus(OrderStatus.ONGOING);
        } else {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "RESOURCE_NOT_FOUND", "Resource not found");
        }
    }

    public List<Order> pendingOrders() {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        if (user.getUserRole() == UserRole.ADMIN) {
            return orderRepository.findByStatus(OrderStatus.PENDING);
        } else {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "RESOURCE_NOT_FOUND", "Resource not found");
        }
    }
    public List<Order> canceledOrders() {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        if (user.getUserRole() == UserRole.ADMIN) {
            return orderRepository.findByStatus(OrderStatus.CANCELLED);
        } else {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "RESOURCE_NOT_FOUND", "Resource not found");
        }
    }

    public List<Order> canceledOwnOrders() {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        return orderRepository.findByStatusAndUserId(OrderStatus.CANCELLED, user.getId());
    }


    public List<Order> pendingOwnOrders() {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        return orderRepository.findByStatusAndUserId(OrderStatus.PENDING, user.getId());
    }


    public List<Order> ongoingOwnOrders() {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

//        return orderRepository.findByStatusAndUserId(OrderStatus.ONGOING, user.getId());
        // Exclude PENDING and CANCELLED statuses
        List<OrderStatus> excludedStatuses = List.of(OrderStatus.PENDING, OrderStatus.CANCELLED);
        return orderRepository.findByStatusNotInAndUserId(excludedStatuses, user.getId());
    }

    public ApplicationResponseDTO updateOrderStatus(Long orderId, String status) {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));

        if (user.getUserRole() == UserRole.ADMIN) {
            orderRepository.findByIdAndStatusNot(orderId, OrderStatus.CANCELLED)
                    .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ORDER_CANCELLED", "Cannot change the status of cancelled order"));
            Order order = orderRepository.findById(orderId).get();
            if (status.equals("PENDING")){
                order.setStatus(OrderStatus.PENDING);
            }
//            order.setStatus(OrderStatus.valueOf(status));
            orderRepository.save(order);
            return new ApplicationResponseDTO(HttpStatus.OK, "ORDER_STATUS_UPDATED_SUCCESSFULLY", "Order status updated successfully!");
        } else {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "RESOURCE_NOT_FOUND", "Resource not found");
        }
    }

}
