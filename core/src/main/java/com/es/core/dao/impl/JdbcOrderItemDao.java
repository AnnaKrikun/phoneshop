package com.es.core.dao.impl;

import com.es.core.configurer.order.OrderItemParametersPreparer;
import com.es.core.dao.OrderItemDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
@Transactional(rollbackFor = OutOfStockException.class)
public class JdbcOrderItemDao implements OrderItemDao {
    private final static String DELETE_ORDER_ITEMS = "delete from orderItems where id = ?";
    private final static String UPDATE_ORDER_ITEM_QUERY = "update orderItems set orderId = ?, phoneId = ?, quantity = ?"
            + "where id = ?";
    private static final String ORDER_ITEMS_TABLE_NAME = "orderItems";
    private static final String GENERATED_KEY_COLUMN = "id";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private OrderItemParametersPreparer orderItemParametersPreparer;

    @Autowired
    public JdbcOrderItemDao(JdbcTemplate jdbcTemplate, OrderItemParametersPreparer orderItemParametersPreparer) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderItemParametersPreparer = orderItemParametersPreparer;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(ORDER_ITEMS_TABLE_NAME)
                .usingGeneratedKeyColumns(GENERATED_KEY_COLUMN);
    }

    @Override
    public void saveOrderItems(List<OrderItem> orderItems) throws OutOfStockException {
        orderItems.forEach(orderItem -> saveOrderItem(orderItem));
    }

    @Override
    public void saveOrderItem(OrderItem orderItem) {
        try {
            Map<String, Object> parameters = orderItemParametersPreparer.fillMapForSaving(orderItem);
            Long newId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
            orderItem.setId(newId);
        } catch (DataAccessException e) {
            throw new OutOfStockException();
        }
    }

    @Override
    public void updateOrderItem(OrderItem orderItem) {
        Object[] preparedParameters = orderItemParametersPreparer.getPreparedParameters(orderItem);
        jdbcTemplate.update(UPDATE_ORDER_ITEM_QUERY, preparedParameters);
    }

    @Override
    public void updateOrderItems(List<OrderItem> orderItems) {
        orderItems.forEach(orderItem -> updateOrderItems(orderItems));
    }

    @Override
    public void deleteOrderItems(List<OrderItem> orderItems) {
        jdbcTemplate.batchUpdate(DELETE_ORDER_ITEMS, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, orderItems.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return orderItems.size();
            }
        });
    }


}
