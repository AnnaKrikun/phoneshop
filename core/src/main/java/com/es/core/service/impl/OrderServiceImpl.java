package com.es.core.service.impl;

import com.es.core.dao.OrderDao;
import com.es.core.dao.OrderItemDao;
import com.es.core.dao.StockDao;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.service.CartService;
import com.es.core.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = OutOfStockException.class)
public class OrderServiceImpl implements OrderService {
    @Value("${deliveryPrice}")
    private BigDecimal deliveryPrice;

    private CartService cartService;
    private OrderDao orderDao;
    private OrderItemDao orderItemDao;
    private StockDao stockDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, CartService cartService, OrderItemDao orderItemDao, StockDao stockDao) {
        this.orderDao = orderDao;
        this.cartService = cartService;
        this.orderItemDao = orderItemDao;
        this.stockDao = stockDao;
    }

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setSubtotal(cart.getTotalPrice());
        order.setDeliveryPrice(deliveryPrice);
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> new OrderItem(cartItem.getPhone(), order, cartItem.getQuantity()))
                .collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setTotalPrice(deliveryPrice.add(cart.getTotalPrice()));
        return order;
    }

    @Override
    public void placeOrder(Order order) {
        order.setDate(new Date());
        orderDao.save(order);
        orderItemDao.save(order.getOrderItems());
        cartService.clearCart();
    }

    @Override
    public Optional<Order> get(Long orderId) {
        return orderDao.get(orderId);
    }

    @Override
    public List<Order> getAll(int offset, int limit) {
        return orderDao.getAll(offset, limit);
    }

    @Override
    public int countOrders() {
        return orderDao.countOrders();
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus orderStatus) {
        Order order = get(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (order.getStatus() != OrderStatus.NEW || orderStatus == OrderStatus.NEW) {
            throw new IllegalArgumentException();
        }
        orderDao.updateOrderStatus(id, orderStatus);
        Map<OrderStatus, Map<Long, Long>> mapForUpdate = createMapForUpdate(order, orderStatus);
        stockDao.update(mapForUpdate);
        order.setStatus(orderStatus);
    }

    private Map<OrderStatus, Map<Long, Long>> createMapForUpdate(Order order, OrderStatus orderStatus) {
        Map<OrderStatus, Map<Long, Long>> mapForUpdate = new HashMap<>();
        Map<Long, Long> phoneIdsWithQuantities = order.getOrderItems().stream()
                .collect(Collectors.toMap(orderItem -> orderItem.getPhone().getId(), orderItem -> orderItem.getQuantity()));
        mapForUpdate.put(orderStatus, phoneIdsWithQuantities);
        return mapForUpdate;
    }
}
