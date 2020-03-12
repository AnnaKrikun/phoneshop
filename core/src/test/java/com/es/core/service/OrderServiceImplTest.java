package com.es.core.service;

import com.es.core.dao.OrderDao;
import com.es.core.dao.OrderItemDao;
import com.es.core.dao.StockDao;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import com.es.core.service.impl.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {
    private final Long ORDER_ID = 1000L;
    private final int ORDER_COUNT = 1000;
    private final OrderStatus ORDER_STATUS_NEW = OrderStatus.NEW;
    private final OrderStatus ORDER_STATUS_REJECTED = OrderStatus.REJECTED;
    private final OrderStatus ORDER_STATUS_DELIVERED = OrderStatus.DELIVERED;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderItemDao orderItemDao;
    @Mock
    private StockDao stockDao;
    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl(orderDao, cartService, orderItemDao, stockDao);

    private BigDecimal deliveryPrice = new BigDecimal(5);
    private List<Phone> phoneList;

    @Before
    public void init() {
        initDeliveryPrice();
        initPhoneList();
    }

    public void initDeliveryPrice() {
        ReflectionTestUtils.setField(orderService, "deliveryPrice", deliveryPrice);
    }

    @Test
    public void ShouldCreateOrderCorrectly() {
        final BigDecimal COST = new BigDecimal(1000);
        Cart cart = new Cart();
        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(phoneList.get(0), 1L));
        items.add(new CartItem(phoneList.get(1), 2L));
        items.add(new CartItem(phoneList.get(2), 3L));
        cart.setCartItems(items);
        cart.setTotalPrice(COST);

        Order order = orderService.createOrder(cart);

        assertTrue(order.getSubtotal().equals(COST));
        assertTrue(order.getDeliveryPrice().equals(deliveryPrice));
        assertTrue(order.getTotalPrice().equals(COST.add(deliveryPrice)));

        List<CartItem> settedItems = order.getOrderItems().stream()
                .map(orderItem -> new CartItem(orderItem.getPhone(), orderItem.getQuantity()))
                .collect(Collectors.toList());

        assertEquals(items, settedItems);
    }

    @Test
    public void shouldPlaceOrderSuccessfully() {
        Order order = new Order();

        orderService.placeOrder(order);

        verify(orderDao).save(eq(order));
        verify(orderItemDao).save(order.getOrderItems());
        verify(cartService).clearCart();
    }

    @Test(expected = OutOfStockException.class)
    public void shouldThrowOutOfStockExceptionWhenPlaceOrderOutOfStock() {
        Order order = new Order();
        doThrow(OutOfStockException.class).when(orderItemDao).save(order.getOrderItems());
        try {
            orderService.placeOrder(order);
        } finally {
            verify(orderDao).save(eq(order));
            verify(orderItemDao).save(order.getOrderItems());
        }
    }

    @Test
    public void shouldGetOrderSuccessfully() {
        Order order = createOrder(ORDER_ID, 3);

        when(orderDao.get(ORDER_ID)).thenReturn(Optional.of(order));

        Order newOrder = orderService.get(ORDER_ID).get();

        verify(orderDao).get(eq(ORDER_ID));

        assertEquals(order, newOrder);
    }

    @Test
    public void shouldGetNotExistingOrderSuccessfully() {
        when(orderDao.get(ORDER_ID)).thenReturn(Optional.empty());

        Optional<Order> orderOptional = orderService.get(ORDER_ID);

        verify(orderDao).get(eq(ORDER_ID));

        assertFalse(orderOptional.isPresent());
    }

    @Test
    public void shouldGetAllCorrectly() {
        final int OFFSET = 0;
        final int LIMIT = 10;
        final int COUNT = 5;
        List<Order> orderList = new ArrayList<>();
        for (int i = 1; i <= COUNT; i++) {
            orderList.add(createOrder((long) i, i));
        }

        when(orderDao.getAll(OFFSET, LIMIT)).thenReturn(orderList);

        List<Order> findAllOrderList = orderService.getAll(OFFSET, LIMIT);

        verify(orderDao).getAll(OFFSET, LIMIT);

        assertEquals(findAllOrderList, orderList);
    }

    @Test
    public void shouldCountOrdersCorrectly() {
        when(orderDao.countOrders()).thenReturn(ORDER_COUNT);

        int count = orderService.countOrders();

        verify(orderDao).countOrders();

        assertEquals(ORDER_COUNT, count);
    }

    @Test
    public void shouldUpdateOrderStatusCorrectly() {
        Order order = createOrder(ORDER_ID, 3);

        when(orderDao.get(ORDER_ID)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(ORDER_ID, ORDER_STATUS_DELIVERED);

        verify(orderDao).updateOrderStatus(ORDER_ID, ORDER_STATUS_DELIVERED);
    }

    @Test(expected = OrderNotFoundException.class)
    public void shouldThrowOrderNotFoundExceptionWhenUpdateNotExistingOrderStatus() {
        when(orderDao.get(ORDER_ID)).thenReturn(Optional.empty());
        try {
            orderService.updateOrderStatus(ORDER_ID, ORDER_STATUS_DELIVERED);
        } finally {
            verify(orderDao).get(eq(ORDER_ID));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenUpdateOrderStatusSetNew() {
        Order order = createOrder(ORDER_ID, 3);

        when(orderDao.get(ORDER_ID)).thenReturn(Optional.of(order));
        try {
            orderService.updateOrderStatus(ORDER_ID, ORDER_STATUS_NEW);
        } finally {
            verify(orderDao).get(eq(ORDER_ID));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenUpdateOrderWithNotNewStatus() {
        Order order = createOrder(ORDER_ID, 3);
        order.setStatus(OrderStatus.DELIVERED);

        when(orderDao.get(ORDER_ID)).thenReturn(Optional.of(order));
        try {
            orderService.updateOrderStatus(ORDER_ID, ORDER_STATUS_REJECTED);
        } finally {
            verify(orderDao).get(eq(ORDER_ID));
        }
    }

    private void initPhoneList() {
        phoneList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            phoneList.add(createPhone(Integer.toString(i), Integer.toString(i), (long) i));
        }
    }

    private Phone createPhone(String brand, String model, Long id) {
        Phone phone = new Phone();
        phone.setBrand(brand);
        phone.setModel(model);
        phone.setPrice(new BigDecimal(100));
        phone.setId(id);
        return phone;
    }

    protected Order createOrder(Long id, int phoneCount) {
        Order order = new Order();
        order.setId(id);
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < phoneCount; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPhone(phoneList.get(i));
            orderItem.setQuantity((long) (i + 1));
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        BigDecimal cost = orderItems.stream()
                .map(e -> e.getPhone().getPrice().multiply(new BigDecimal(e.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setSubtotal(cost);

        BigDecimal delivery = new BigDecimal(5);
        order.setDeliveryPrice(delivery);
        order.setTotalPrice(cost.add(delivery));

        order.setStatus(OrderStatus.NEW);
        order.setFirstName("test");
        order.setLastName("test");
        order.setDeliveryAddress("Minsk");
        order.setContactPhoneNo("+375291234567");
        return order;
    }

}
