package com.es.core.dao.impl;

import com.es.core.dao.OrderItemDao;
import com.es.core.dao.StockDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.OrderItem;
import com.es.core.preparer.order.OrderItemParametersPreparer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class JdbcOrderItemDao implements OrderItemDao {
    Logger logger = Logger.getLogger(JdbcOrderItemDao.class);

    private static final String ORDER_ITEMS_TABLE_NAME = "orderItems";
    private static final String GENERATED_KEY_COLUMN = "id";
    private static final String OUT_OF_STOCK_EXCEPTION = "Out of stock exception";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private OrderItemParametersPreparer orderItemParametersPreparer;
    private StockDao stockDao;

    @Autowired
    public JdbcOrderItemDao(JdbcTemplate jdbcTemplate, OrderItemParametersPreparer orderItemParametersPreparer,
                            StockDao stockDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderItemParametersPreparer = orderItemParametersPreparer;
        this.stockDao = stockDao;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(ORDER_ITEMS_TABLE_NAME)
                .usingGeneratedKeyColumns(GENERATED_KEY_COLUMN);
    }

    @Override
    public void save(List<OrderItem> orderItems) throws OutOfStockException {
        orderItems.forEach(orderItem -> save(orderItem));
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = OutOfStockException.class)
    public void save(OrderItem orderItem) {
        try {
            Map<String, Object> parameters = orderItemParametersPreparer.fillMapForSaving(orderItem);
            Long newId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
            orderItem.setId(newId);
            stockDao.updateNew(orderItem.getPhone().getId(), orderItem.getQuantity());
        } catch (DataAccessException | OutOfStockException e) {
            logger.error(OUT_OF_STOCK_EXCEPTION, e);
            throw new OutOfStockException(e);
        }
    }
}
