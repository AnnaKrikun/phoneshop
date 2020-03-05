package com.es.phoneshop.web.service;

import com.es.phoneshop.web.page.OrderListPage;

public interface OrdersPageService {
    OrderListPage getOrdersPage(int pageNumber);
}
