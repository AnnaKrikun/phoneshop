package com.es.core.dao;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Optional<Order> get(Long key);
    List<Order> getAll(int offset, int limit);
    void save(Order order);
    int countOrders();
    void updateOrderStatus(Long id, OrderStatus orderStatus);
}
