package com.es.phoneshop.web.helper;

import com.es.core.model.order.Order;
import com.es.core.service.CartService;
import com.es.core.service.OrderService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.annotation.Resource;

@Component
public class OrderHelper {
    private static final String ERROR_OUT_OF_STOCK = "errorMessageOutOfStock";
    private static final Object ERROR_MESSAGE_OUT_OF_STOCK = "Out of stock items were deleted.";

    @Resource
    private CartService cartService;
    @Resource
    private OrderService orderService;

    public void processOutOfStockException(Order order, Model model) {
        cartService.deleteOutOfStock();
        Order orderFromCart = orderService.createOrder(cartService.getCart());
        setOrderProperties(orderFromCart, order);
        model.addAttribute(ERROR_OUT_OF_STOCK, ERROR_MESSAGE_OUT_OF_STOCK);
    }

    public void setOrderProperties(Order orderFromCart, Order order) {
        order.setTotalPrice(orderFromCart.getTotalPrice());
        order.setOrderItems(orderFromCart.getOrderItems());
        order.setDeliveryPrice(orderFromCart.getDeliveryPrice());
        order.setSubtotal(orderFromCart.getSubtotal());
        order.setStatus(orderFromCart.getStatus());
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));
    }
}
