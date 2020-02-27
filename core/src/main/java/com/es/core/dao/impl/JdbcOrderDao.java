package com.es.core.dao.impl;

import com.es.core.configurer.order.OrderListResultExtractor;
import com.es.core.configurer.order.OrderParametersPreparer;
import com.es.core.dao.OrderDao;
import com.es.core.dao.OrderItemDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional(rollbackFor = OutOfStockException.class)
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
    private static final String UPDATE_ORDER = "update orders set subtotal = ? ,deliveryPrice = ? ," +
            "totalPrice = ? ,firstName = ? ,lastName = ? ,deliveryAddress = ? ,contactPhoneNo = ? ," +
            "additionalInfo = ? ,status = ? where id = ? ";
    private static final String ORDERS_TABLE_NAME = "orders";
    private static final String GENERATED_KEY_COLUMN = "id";
    private JdbcTemplate jdbcTemplate;
    private OrderListResultExtractor orderListResultExtractor;
    private OrderParametersPreparer orderParametersPreparer;
    private SimpleJdbcInsert simpleJdbcInsert;
    private OrderItemDao orderItemDao;

    @Autowired
    public JdbcOrderDao(JdbcTemplate jdbcTemplate, OrderListResultExtractor orderListResultExtractor,
                        OrderParametersPreparer orderParametersPreparer, OrderItemDao orderItemDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderListResultExtractor = orderListResultExtractor;
        this.orderParametersPreparer = orderParametersPreparer;
        this.orderItemDao = orderItemDao;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(ORDERS_TABLE_NAME)
                .usingGeneratedKeyColumns(GENERATED_KEY_COLUMN);
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
        if (order.getId() == null) {
            saveOrder(order);
        } else {
            updateOrder(order);
        }
    }

    private void updateOrder(Order order) {
        jdbcTemplate.update(UPDATE_ORDER, orderParametersPreparer.getPreparedParameters(order));
        updateOrderItems(order);
    }

    private void updateOrderItems(Order order) {
        Optional<Order> previousOrderOptional = get(order.getId());
        if (previousOrderOptional.isPresent()) {
            Order previousOrder = previousOrderOptional.get();

            List<OrderItem> orderItemsToDelete = previousOrder.getOrderItems();
            orderItemsToDelete.removeAll(order.getOrderItems());
            orderItemDao.deleteOrderItems(orderItemsToDelete);

            List<OrderItem> orderItemsToUpdate = order.getOrderItems().stream()
                    .filter(orderItem -> orderItem.getId() != null)
                    .collect(Collectors.toList());
            orderItemDao.updateOrderItems(orderItemsToUpdate);

            List<OrderItem> orderItemsToSave = order.getOrderItems().stream()
                    .filter(orderItem -> orderItem.getId() == null)
                    .collect(Collectors.toList());
            orderItemDao.saveOrderItems(orderItemsToSave);
        }
    }

    private void saveOrder(Order order) {
        try {
            SqlParameterSource sqlParameterSource = orderParametersPreparer.fillMapForSavingSQL(order);
            Long orderId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
            order.setId(orderId);
            orderItemDao.saveOrderItems(order.getOrderItems());
        } catch (DataAccessException e) {
            throw new OutOfStockException();
        }
    }
}
