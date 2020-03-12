package com.es.phoneshop.web.page;

import com.es.core.model.order.Order;

import java.util.List;

public class OrderListPage {
    private List<Order> orders;
    private Pagination pagination;

    public OrderListPage() {
    }

    public OrderListPage(List<Order> orders, Pagination pagination) {
        this.orders = orders;
        this.pagination = pagination;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
