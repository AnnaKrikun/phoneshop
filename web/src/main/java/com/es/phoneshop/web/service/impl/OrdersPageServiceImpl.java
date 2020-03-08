package com.es.phoneshop.web.service.impl;

import com.es.core.model.order.Order;
import com.es.core.service.OrderService;
import com.es.phoneshop.web.page.OrderListPage;
import com.es.phoneshop.web.page.Pagination;
import com.es.phoneshop.web.service.OrdersPageService;
import com.es.phoneshop.web.service.PageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrdersPageServiceImpl implements OrdersPageService {
    @Resource
    private PageService pageService;
    @Resource
    private OrderService orderService;

    @Override
    public OrderListPage getOrdersPage(int pageNumber) {
        int itemsNumber = orderService.countOrders();
        int normalizedPageNumber = pageService.normalizedPageNumber(itemsNumber, pageNumber);

        int offset = ((normalizedPageNumber - 1) * pageService.AMOUNT_OF_PHONES_ON_PAGE);
        int limit = pageService.AMOUNT_OF_PHONES_ON_PAGE;

        List<Order> orderList = getOrderList(offset, limit);

        Pagination pagination = pageService.getPagination(itemsNumber, pageNumber);

        return new OrderListPage(orderList, pagination);
    }

    private List<Order> getOrderList(int offset, int limit) {
        return orderService.getAll(offset, limit);
    }
}
