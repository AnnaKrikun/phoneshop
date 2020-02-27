package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.service.CartService;
import com.es.core.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.es.phoneshop.web.constants.ControllerConstants.*;

@Controller
@RequestMapping(value = ORDER_MAPPING)
public class OrderPageController {
    private static final String CART = "cart";
    private static final String ORDER = "order";
    private static final String ERROR_OUT_OF_STOCK = "errorMessageOutOfStock";
    private static final Object ERROR_MESSAGE_OUT_OF_STOCK = "Out of stock items were deleted.";
    @Resource
    private OrderService orderService;
    @Resource
    private CartService cartService;

    @GetMapping
    public String getOrder(Model model) throws OutOfStockException {
        Order order = orderService.createOrder(cartService.getCart());
        model.addAttribute(CART, cartService.getCart());
        model.addAttribute(ORDER, order);
        return ORDER_PAGE_NAME;
    }

    @PostMapping
    public String placeOrder(@Valid Order order, BindingResult bindingResult, Model model) {
        Order orderFromCart = orderService.createOrder(cartService.getCart());
        setOrderProperties(orderFromCart, order);
        if (bindingResult.hasErrors()) {
            return ORDER_PAGE_NAME;
        }
        try {
            orderService.placeOrder(order);
            return REDIRECT_ORDER_OVERVIEW + order.getId();
        } catch (OutOfStockException e) {
            processOutOfStockException(order, model);
            return ORDER_PAGE_NAME;
        }
    }

    private void processOutOfStockException(Order order, Model model) {
        cartService.deleteOutOfStock();
        Order orderFromCart = orderService.createOrder(cartService.getCart());
        setOrderProperties(orderFromCart, order);
        model.addAttribute(ERROR_OUT_OF_STOCK, ERROR_MESSAGE_OUT_OF_STOCK);
    }

    private void setOrderProperties(Order orderFromCart, Order order) {
        order.setTotalPrice(orderFromCart.getTotalPrice());
        order.setOrderItems(orderFromCart.getOrderItems());
        order.setDeliveryPrice(orderFromCart.getDeliveryPrice());
        order.setSubtotal(orderFromCart.getSubtotal());
        order.setStatus(orderFromCart.getStatus());
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));
    }
}
