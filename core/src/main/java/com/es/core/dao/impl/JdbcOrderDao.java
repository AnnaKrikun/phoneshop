package com.es.core.dao.impl;

import com.es.core.configurer.order.OrderListResultExtractor;
import com.es.core.dao.OrderDao;
import com.es.core.model.order.Order;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JdbcOrderDao implements OrderDao {

    private static final String SELECT_ORDER_BY_ID_QUERY = "select orders.id AS orderId, subtotal, deliveryPrice," +
            "totalPrice, firstName, lastName, deliveryAddress, contactPhoneNo, status," +
            "additionalInfo, orderItems.id AS orderItemId, quantity," +
            "phones.id AS phoneId, brand, model, price, displaySizeInches, weightGr, lengthMm," +
            "widthMm, heightMm, announced, deviceType, os, displayResolution, " +
            "pixelDensity, displayTechnology, backCameraMegapixels, " +
            "frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, " +
            "talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, " +
            "description, colors.id AS colorId, colors.code AS colorCode from orders " +
            "left join orderItems on orders.id = orderItems.orderId " +
            "left join phones on phones.id = orderItems.phoneId " +
            "left join phone2color on phones.id = phone2color.phoneId " +
            "left join colors on colors.id = phone2color.colorId " +
            "where orders.id = ?";
    private final JdbcTemplate jdbcTemplate;
    private final OrderListResultExtractor orderListResultExtractor;

    @Autowired
    public JdbcOrderDao(JdbcTemplate jdbcTemplate, OrderListResultExtractor orderListResultExtractor) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderListResultExtractor = orderListResultExtractor;
    }

    @Override
    public Optional<Order> get(Long key) {
        List<Order> orders = jdbcTemplate.query(SELECT_ORDER_BY_ID_QUERY, orderListResultExtractor, key);
        if (CollectionUtils.isNotEmpty(orders)) {
            return Optional.of(orders.get(0));
        }
        return Optional.empty();
    }

    @Override
    public void save(Order order) {

    }
}
