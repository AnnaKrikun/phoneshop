package com.es.core.dao;

import com.es.core.model.order.OrderItem;

import java.util.List;

public interface OrderItemDao {
    void saveOrderItems(List<OrderItem> orderItems);

    void saveOrderItem(OrderItem orderItem);

    void updateOrderItem(OrderItem orderItem);

    void updateOrderItems(List<OrderItem> orderItems);

    void deleteOrderItems(List<OrderItem> orderItems);
}
