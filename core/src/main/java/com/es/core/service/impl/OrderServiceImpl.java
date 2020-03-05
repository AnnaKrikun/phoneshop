package com.es.core.service.impl;

import com.es.core.dao.OrderDao;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.service.CartService;
import com.es.core.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Value("${deliveryPrice}")
    private BigDecimal deliveryPrice;

    private CartService cartService;
    private OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, CartService cartService) {
        this.orderDao = orderDao;
        this.cartService = cartService;
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
        cartService.clearCart();
    }

    @Override
    public Optional<Order> getOrder(Long orderId) {
        return orderDao.get(orderId);
    }

    @Override
    public List<Order> getAllOrders(int offset, int limit) {
        return orderDao.getAll(offset, limit);
    }

    @Override
    public int countOrders() {
        return orderDao.countOrders();
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus orderStatus) {
        Order order = getOrder(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (order.getStatus() != OrderStatus.NEW || orderStatus == OrderStatus.NEW) {
            throw new IllegalArgumentException();
        }
        orderDao.updateOrderStatus(id, orderStatus);
        order.setStatus(orderStatus);
    }
}
