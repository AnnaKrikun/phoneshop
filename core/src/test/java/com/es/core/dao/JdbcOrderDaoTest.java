package com.es.core.dao;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import org.junit.Before;
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
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/context/test-config.xml")
public class JdbcOrderDaoTest {
    private static final String COUNT_ORDERS = "select count(*) from orders";
    private static final Long ADDED_ORDERS = 1L;
    private static final String ERROR_IN_ORDER_SAVE = "Error: Number of added / found orders = ";
    private static final Long EXISTING_KEY = 1001L;
    private static final String ERROR_IN_EXISTING_KEY = "Result for existing key is empty";
    private static final Long NOT_EXISTING_KEY = 9999L;
    private static final int EXPECTED_ORDERS_AMOUNT = 2;
    private static final String ERROR_IN_ORDERS_AMOUNT = "Error: Actual / Expected amount = ";
    private static final int OFFSET_SUCCESS = 0;
    private static final int LIMIT_SUCCESS = 2;
    private static final String ERROR_IN_GET_ALL_ORDERS = "Error: Number of found / expected orders = ";
    private static final String ERROR_IN_STATUS = "Error in status";
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private JdbcTemplate jdbcTemplateTest;

    private Optional<Order> optionalOrder;
    private List<Order> orders;
    private List<OrderItem> orderItems;
    private OrderItem orderItem;
    private Phone phone;
    private Order order;

    @Before
    public void init() {
        order = new Order();
        orderItem = new OrderItem();
        phone = new Phone();
        orderItems = new ArrayList<>();

        phone.setId(1002L);
        orderItem.setPhone(phone);
        orderItem.setQuantity(1L);
        orderItem.setOrder(order);

        orderItems.add(orderItem);
        phone.setId(1003L);
        orderItem.setPhone(phone);
        orderItems.add(orderItem);

        order.setOrderItems(orderItems);
        order.setFirstName("Ivan");
        order.setLastName("Ivanov");
        order.setDeliveryAddress("Minsk");
        order.setContactPhoneNo("+375291234567");
        order.setStatus(OrderStatus.NEW);
    }

    @Test
    @DirtiesContext
    public void shouldSaveOrderSuccessfully() {
        Long amountBeforeSave = jdbcTemplateTest.queryForObject(COUNT_ORDERS, Long.class);

        orderDao.save(order);

        Long amountAfterSave = jdbcTemplateTest.queryForObject(COUNT_ORDERS, Long.class);

        Assert.isTrue(ADDED_ORDERS.equals(amountAfterSave - amountBeforeSave),
                ERROR_IN_ORDER_SAVE + ADDED_ORDERS + " / " + (amountAfterSave - amountBeforeSave));
    }

    @Test
    @DirtiesContext
    public void shouldDoNothingWhenSaveOrderWithId() {
        Long amountBeforeSave = jdbcTemplateTest.queryForObject(COUNT_ORDERS, Long.class);

        order.setId(1999L);
        orderDao.save(order);

        Long amountAfterSave = jdbcTemplateTest.queryForObject(COUNT_ORDERS, Long.class);

        Assert.isTrue(amountAfterSave.equals(amountBeforeSave),
                ERROR_IN_ORDER_SAVE + 0 + " / " + (amountAfterSave - amountBeforeSave));
    }

    @Test
    @DirtiesContext
    public void shouldGetOrderByIdSuccessfully() {
        optionalOrder = orderDao.get(EXISTING_KEY);

        Assert.isTrue(optionalOrder.isPresent(), ERROR_IN_EXISTING_KEY);
    }

    @Test
    @DirtiesContext
    public void shouldNotGetOrderByNotExistingId() {
        optionalOrder = orderDao.get(NOT_EXISTING_KEY);

        Assert.isTrue(!optionalOrder.isPresent(), ERROR_IN_EXISTING_KEY);
    }

    @Test
    @DirtiesContext
    public void shouldCountOrdersSuccessfully() {
        int actualOrdersAmount = orderDao.countOrders();

        Assert.isTrue(actualOrdersAmount==EXPECTED_ORDERS_AMOUNT, actualOrdersAmount
                + "/"+ ERROR_IN_ORDERS_AMOUNT);
    }

    @Test
    @DirtiesContext
    public void shouldFindAllPhonesSuccessfully() {
        orders = orderDao.getAll(OFFSET_SUCCESS, LIMIT_SUCCESS);

        Assert.isTrue(orders.size() == LIMIT_SUCCESS,
                ERROR_IN_GET_ALL_ORDERS + orders.size() + " " + LIMIT_SUCCESS);
    }

    @Test(expected = OrderNotFoundException.class)
    @DirtiesContext
    public void shouldThrowOrderNotFoundExceptionWhenUpdateNotExistingOrder() {
        orderDao.updateOrderStatus(NOT_EXISTING_KEY, OrderStatus.DELIVERED);
    }

    @Test
    @DirtiesContext
    public void shouldUpdateOrderStatusToRejectedSuccessfully() {
        optionalOrder = orderDao.get(EXISTING_KEY);
        Assert.isTrue(optionalOrder.isPresent(), ERROR_IN_EXISTING_KEY);
        Assert.isTrue(optionalOrder.get().getStatus()==OrderStatus.NEW, ERROR_IN_STATUS);

        orderDao.updateOrderStatus(EXISTING_KEY, OrderStatus.REJECTED);

        optionalOrder = orderDao.get(EXISTING_KEY);
        Assert.isTrue(optionalOrder.isPresent(), ERROR_IN_EXISTING_KEY);
        Assert.isTrue(optionalOrder.get().getStatus()==OrderStatus.REJECTED, ERROR_IN_STATUS);
    }

    @Test
    @DirtiesContext
    public void shouldUpdateOrderStatusToDeliveredSuccessfully() {
        optionalOrder = orderDao.get(EXISTING_KEY);
        Assert.isTrue(optionalOrder.isPresent(), ERROR_IN_EXISTING_KEY);
        Assert.isTrue(optionalOrder.get().getStatus()==OrderStatus.NEW, ERROR_IN_STATUS);

        orderDao.updateOrderStatus(EXISTING_KEY, OrderStatus.DELIVERED);

        optionalOrder = orderDao.get(EXISTING_KEY);
        Assert.isTrue(optionalOrder.isPresent(), ERROR_IN_EXISTING_KEY);
        Assert.isTrue(optionalOrder.get().getStatus()==OrderStatus.DELIVERED, ERROR_IN_STATUS);
    }


}
