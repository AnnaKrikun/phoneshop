package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.service.CartService;
import com.es.core.service.OrderService;
import com.es.phoneshop.web.helper.OrderHelper;
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

    @Resource
    private OrderService orderService;
    @Resource
    private CartService cartService;
    @Resource
    private OrderHelper orderHelper;

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
        orderHelper.setOrderProperties(orderFromCart, order);
        if (bindingResult.hasErrors()) {
            return ORDER_PAGE_NAME;
        }
        try {
            orderService.placeOrder(order);
            return REDIRECT_ORDER_OVERVIEW + order.getId();
        } catch (OutOfStockException e) {
            orderHelper.processOutOfStockException(order, model);
            return ORDER_PAGE_NAME;
        }
    }


}
