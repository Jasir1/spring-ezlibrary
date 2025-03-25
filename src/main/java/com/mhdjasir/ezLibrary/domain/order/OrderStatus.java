package com.mhdjasir.ezLibrary.domain.order;

public enum OrderStatus {
    PENDING,
    ONGOING,
    CANCELLED,
    ORDER_RECEIVED,
    ORDER_PROCESSING,
    PAYMENT_CONFIRMED,
    ORDER_PACKED,
    ORDER_DISPATCHED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERY_ATTEMPTED,
    DELIVERED,
    RETURNED_TO_SENDER,
    REFUNDED,
}
