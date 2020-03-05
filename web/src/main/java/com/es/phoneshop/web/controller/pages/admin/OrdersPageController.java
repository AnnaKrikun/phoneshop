package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.service.OrderService;
import com.es.phoneshop.web.exception.PageNotFoundException;
import com.es.phoneshop.web.service.OrdersPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.es.phoneshop.web.constants.ControllerConstants.*;

@Controller
@RequestMapping(value = ADMIN_ORDERS_MAPPING)
public class OrdersPageController {
    private static final String PAGE_REQUEST_PARAM = "page";
    private static final String PAGE_DEFAULT_VALUE = "1";
    private static final String ORDER_PAGE = "orderPage";
    private static final String ORDER = "order";
    private static final String ID = "/{id}";

    @Resource
    private OrdersPageService ordersPageService;
    @Resource
    private OrderService orderService;

    @GetMapping
    public String showOrders(@RequestParam(name = PAGE_REQUEST_PARAM, defaultValue = PAGE_DEFAULT_VALUE) int pageNumber,
                             Model model) {
        model.addAttribute(ORDER_PAGE, ordersPageService.getOrdersPage(pageNumber));
        return ADMIN_ORDERS_PAGE_NAME;
    }

    @GetMapping(ID)
    public String showOrders(@PathVariable Long id, Model model) {
        Order order = orderService.getOrder(id).orElseThrow(PageNotFoundException::new);
        model.addAttribute(ORDER, order);
        return ADMIN_ORDER_DETAILS_PAGE_NAME;
    }

    @PostMapping(ID)
    public String updateOrderStatus(@PathVariable Long id, OrderStatus orderStatus) {
        orderService.updateOrderStatus(id, orderStatus);
        return REDIRECT_ADMIN_ORDER_DETAILS + id;
    }

    @ExceptionHandler({PageNotFoundException.class, OrderNotFoundException.class})
    public String pageNotFound() {
        return NOT_FOUND_PAGE_NAME;
    }
    @ExceptionHandler({IllegalArgumentException.class})
    public String badRequest(){
        return BAD_UPDATE_STATUS_REQUEST_PAGE_NAME;
    }
}
