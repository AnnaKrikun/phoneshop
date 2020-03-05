package com.es.core.service;

import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(Cart cart);
    void placeOrder(Order order) throws OutOfStockException;
    Optional<Order> getOrder(Long orderId);
    List<Order> getAllOrders(int offset, int limit);
    int countOrders();
    void updateOrderStatus(Long id, OrderStatus orderStatus);
}
