package com.es.core.dao;

import com.es.core.model.order.OrderItem;

import java.util.List;

public interface OrderItemDao {
    void save(List<OrderItem> orderItems);

    void save(OrderItem orderItem);
}
