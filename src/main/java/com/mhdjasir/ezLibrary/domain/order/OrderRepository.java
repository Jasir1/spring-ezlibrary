package com.mhdjasir.ezLibrary.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndUserIdAndStatus(Long orderId, Long id, OrderStatus orderStatus);

    Optional<Order> findByIdAndUserId(Long orderId, Long id);

    List<Order> findAllByUserId(Long id);

    Optional<Order> findByIdAndStatus(Long orderId, OrderStatus orderStatus);
    Optional<Order> findByIdAndStatusNot(Long orderId, OrderStatus orderStatus);

    List<Order> findByStatus(OrderStatus orderStatus);

    List<Order> findByStatusAndUserId(OrderStatus orderStatus, Long id);

    List<Order> findByStatusNotInAndUserId(List<OrderStatus> excludedStatuses, Long id);
}
