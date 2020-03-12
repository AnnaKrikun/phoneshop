package com.es.phoneshop.web.controller.pages;

import com.es.core.model.order.Order;
import com.es.core.service.OrderService;
import com.es.phoneshop.web.exception.PageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.es.phoneshop.web.constants.ControllerConstants.*;

@Controller
@RequestMapping(value = ORDER_OVERVIEW_MAPPING)
public class OrderOverviewPageController {
    private static final String ORDER = "order";
    private OrderService orderService;

    @Autowired
    public OrderOverviewPageController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getOrderOverview(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrder(orderId).orElseThrow(PageNotFoundException::new);
        model.addAttribute(ORDER, order);
        return ORDER_OVERVIEW_PAGE_NAME;
    }

    @ExceptionHandler({PageNotFoundException.class})
    public String pageNotFound() {
        return NOT_FOUND_PAGE_NAME;
    }
}
