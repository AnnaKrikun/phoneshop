package com.es.core.dao;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Phone;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/context/test-config.xml")
public class JdbcOrderItemDaoTest {
    private static final String COUNT_ORDER_ITEMS = "select count (*) from orderItems";
    private static final Long ADDED_ORDER_ITEM = 1L;
    private static final String ERROR_IN_PHONE_SAVE = "Error: Number of added / found orderItems = ";;
    private static final Long ADDED_ORDER_ITEMS = 2L;
    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private JdbcTemplate jdbcTemplateTest;

    @Autowired
    private StockDao stockDao;

    private OrderItem orderItem;
    private Phone phone;
    private Order order;

    @Before
    public void init() {
        order = new Order();
        order.setId(1001L);

        phone = new Phone();
        phone.setId(1002L);

        orderItem = new OrderItem();
        orderItem.setPhone(phone);
        orderItem.setQuantity(1L);
        orderItem.setOrder(order);
    }

    @Test
    @DirtiesContext
    public void shouldSaveOrderItemSuccessfully() {
        Long amountBeforeSave = jdbcTemplateTest.queryForObject(COUNT_ORDER_ITEMS, Long.class);

        orderItemDao.saveOrderItem(orderItem);

        Long amountAfterSave = jdbcTemplateTest.queryForObject(COUNT_ORDER_ITEMS, Long.class);

        Assert.isTrue(ADDED_ORDER_ITEM.equals(amountAfterSave - amountBeforeSave),
                ERROR_IN_PHONE_SAVE + ADDED_ORDER_ITEM + " / " + (amountAfterSave - amountBeforeSave));
    }

    @Ignore
    @Test
    @DirtiesContext
    public void shouldThrowOutOfStockWhenAddOutOfStock() {
        Long amountBeforeSave = jdbcTemplateTest.queryForObject(COUNT_ORDER_ITEMS, Long.class);

        phone.setId(1004L);
        orderItem.setPhone(phone);
        orderItem.setQuantity(15L);

        orderItemDao.saveOrderItem(orderItem);

        Long amountAfterSave = jdbcTemplateTest.queryForObject(COUNT_ORDER_ITEMS, Long.class);

        Assert.isTrue(ADDED_ORDER_ITEM.equals(amountAfterSave - amountBeforeSave),
                ERROR_IN_PHONE_SAVE + ADDED_ORDER_ITEM + " / " + (amountAfterSave - amountBeforeSave));
    }

    @Test
    @DirtiesContext
    public void shouldSaveOrderItemsSuccessfully() {
        Long amountBeforeSave = jdbcTemplateTest.queryForObject(COUNT_ORDER_ITEMS, Long.class);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        orderItem.setId(1003L);
        orderItems.add(orderItem);

        orderItemDao.saveOrderItems(orderItems);

        Long amountAfterSave = jdbcTemplateTest.queryForObject(COUNT_ORDER_ITEMS, Long.class);

        Assert.isTrue(ADDED_ORDER_ITEMS.equals(amountAfterSave - amountBeforeSave),
                ERROR_IN_PHONE_SAVE + ADDED_ORDER_ITEMS + " / " + (amountAfterSave - amountBeforeSave));
    }
}
