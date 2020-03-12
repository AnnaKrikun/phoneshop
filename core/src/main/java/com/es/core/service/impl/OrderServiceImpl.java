package com.es.core.service.impl;

import com.es.core.dao.OrderDao;
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

import java.math.BigDecimal;
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
    public void placeOrder(Order order) throws OutOfStockException {
        orderDao.save(order);
        cartService.clearCart();
    }

    @Override
    public Optional<Order> getOrder(Long orderId) {
        return orderDao.get(orderId);
    }
}
