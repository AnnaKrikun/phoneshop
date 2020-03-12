package com.es.phoneshop.web.controller.pages;

import com.es.core.service.OrderService;
import com.es.core.exception.OutOfStockException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

import static com.es.phoneshop.web.constants.ControllerConstants.ORDER_MAPPING;

@Controller
@RequestMapping(value = ORDER_MAPPING)
public class OrderPageController {
    @Resource
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public void getOrder() throws OutOfStockException {
        orderService.createOrder(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void placeOrder() throws OutOfStockException {
        orderService.placeOrder(null);
    }
}
