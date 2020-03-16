package com.es.core.dao.impl;

import com.es.core.dao.OrderDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.preparer.order.OrderListResultExtractor;
import com.es.core.preparer.order.OrderParametersPreparer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional(rollbackFor = OutOfStockException.class)
public class JdbcOrderDao implements OrderDao {
    Logger logger = Logger.getLogger(JdbcOrderDao.class);

    private static final String SELECT_ORDER_BY_ID_QUERY = "select orders.id AS orderId, subtotal, deliveryPrice," +
            "totalPrice, firstName, lastName, deliveryAddress, contactPhoneNo, date, status," +
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
    private static final String SELECT_ALL_ORDERS_QUERY = "select limitedOrders.id AS orderId, subtotal," +
            "deliveryPrice, totalPrice, firstName, lastName, deliveryAddress, contactPhoneNo, date, status," +
            "additionalInfo, orderItems.id AS orderItemId, quantity," +
            "phones.id AS phoneId, brand, model, price, displaySizeInches, weightGr, lengthMm, widthMm, " +
            "heightMm, announced, deviceType, os, displayResolution, " +
            "pixelDensity, displayTechnology, backCameraMegapixels, " +
            "frontCameraMegapixels, ramGb, internalStorageGb, batteryCapacityMah, " +
            "talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl, " +
            "description, colors.id AS colorId, colors.code AS colorCode from " +
            "(select * from orders order by id desc offset ? limit ?) as limitedOrders " +
            "left join orderItems on limitedOrders.id = orderItems.orderId " +
            "left join phones on phones.id = orderItems.phoneId " +
            "left join phone2color on phones.id = phone2color.phoneId " +
            "left join colors on colors.id = phone2color.colorId ";
    private final static String COUNT_ORDERS_QUERY = "select COUNT(1) from orders";
    private final static String UPDATE_ORDER_STATUS = "update orders set status = ? where id = ?";
    private static final String ORDERS_TABLE_NAME = "orders";
    private static final String GENERATED_KEY_COLUMN = "id";
    private static final String OUT_OF_STOCK_EXCEPTION = "Out of stock exception";

    private JdbcTemplate jdbcTemplate;
    private OrderListResultExtractor orderListResultExtractor;
    private OrderParametersPreparer orderParametersPreparer;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public JdbcOrderDao(JdbcTemplate jdbcTemplate, OrderListResultExtractor orderListResultExtractor,
                        OrderParametersPreparer orderParametersPreparer) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderListResultExtractor = orderListResultExtractor;
        this.orderParametersPreparer = orderParametersPreparer;
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
    public List<Order> getAll(int offset, int limit) {
        return jdbcTemplate.query(SELECT_ALL_ORDERS_QUERY, orderListResultExtractor, offset, limit);
    }

    @Override
    public void save(Order order) {
        if (order.getId() == null) {
            saveOrder(order);
        }
    }

    @Override
    public int countOrders() {
        return jdbcTemplate.queryForObject(COUNT_ORDERS_QUERY, Integer.class);
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus orderStatus) {
        jdbcTemplate.update(UPDATE_ORDER_STATUS, orderStatus.name(), id);
    }

    private void saveOrder(Order order) {
        try {
            SqlParameterSource sqlParameterSource = orderParametersPreparer.fillMapForSavingSQL(order);
            Long orderId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
            order.setId(orderId);
        } catch (DataAccessException e) {
            logger.error(OUT_OF_STOCK_EXCEPTION, e);
            throw new OutOfStockException(e);
        }
    }
}
