package com.es.core.dao;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Stock;
import com.es.core.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/context/test-config.xml")
public class JdbcStockDaoTest {
    private static final String ERROR_IN_GET_STOCKS = "Error: Number of found / expected stocks = ";
    private static final int EXPECTED = 3;
    private static final int EXPECTED_POSITIVE = 2;
    private static final String ERROR_STOCK_NOT_FOUND = "Error: Stock not found";
    private static final String ERROR_STOCK_FOUND = "Error: Stock found";
    private static final String ERROR_INCORRECT_RESERVED_AMOUNT = "Error: incorrect reserved amount";
    private static final Long NOT_EXISTING_ID = 100L;
    private static final Long RESERVED_AMOUNT = 1L;
    @Autowired
    private StockDao stockDao;

    private List<Long> phoneIds;

    @Before
    public void init() {
        phoneIds = new ArrayList<>(3);
        phoneIds.add(1001L);
        phoneIds.add(1002L);
        phoneIds.add(1003L);
    }

    @Test
    @DirtiesContext
    public void shouldGetStocksByIds() {
        List<Stock> stocks = stockDao.getByPhoneIds(phoneIds);

        Assert.isTrue(stocks.size() == EXPECTED, ERROR_IN_GET_STOCKS + stocks.size() +
                " / " + EXPECTED);
    }


    @Test
    @DirtiesContext
    public void shouldGetPositiveStocksByIds() {
        List<Stock> stocks = stockDao.getPositiveByPhoneIds(phoneIds);

        Assert.isTrue(stocks.size() == EXPECTED_POSITIVE, ERROR_IN_GET_STOCKS + stocks.size() +
                " / " + EXPECTED_POSITIVE);
    }

    @Test
    @DirtiesContext
    public void shouldGetStockById() {
        Optional<Stock> stock = stockDao.getByPhoneId(phoneIds.get(1));

        Assert.isTrue(stock.isPresent(), ERROR_STOCK_NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void shouldNotGetStockByNotExistingId() {
        Optional<Stock> stock = stockDao.getByPhoneId(NOT_EXISTING_ID);

        Assert.isTrue(!stock.isPresent(), ERROR_STOCK_FOUND);
    }

    @Test
    @DirtiesContext
    public void shouldUpdateStockById() {
        Optional<Stock> stock = stockDao.getByPhoneId(phoneIds.get(1));
        Assert.isTrue(stock.isPresent(), ERROR_STOCK_NOT_FOUND);
        Assert.isTrue(stock.get().getReserved() == 1, ERROR_INCORRECT_RESERVED_AMOUNT);

        stockDao.updateNew(phoneIds.get(1), RESERVED_AMOUNT);

        stock = stockDao.getByPhoneId(phoneIds.get(1));
        Assert.isTrue(stock.isPresent(), ERROR_STOCK_NOT_FOUND);
        Assert.isTrue(stock.get().getReserved() == RESERVED_AMOUNT + 1, ERROR_INCORRECT_RESERVED_AMOUNT);
    }

    @Test(expected = OutOfStockException.class)
    @DirtiesContext
    public void shouldThrowOutOfStockExceptionWhenUpdateOutOfStockById() {
        Optional<Stock> stock = stockDao.getByPhoneId(phoneIds.get(1));
        Assert.isTrue(stock.isPresent(), ERROR_STOCK_NOT_FOUND);
        Assert.isTrue(stock.get().getReserved() == 1, ERROR_INCORRECT_RESERVED_AMOUNT);

        stockDao.updateNew(phoneIds.get(1), 15L);

        stock = stockDao.getByPhoneId(phoneIds.get(1));
        Assert.isTrue(stock.isPresent(), ERROR_STOCK_NOT_FOUND);
        Assert.isTrue(stock.get().getReserved() == 1, ERROR_INCORRECT_RESERVED_AMOUNT);
    }

    @Test
    @DirtiesContext
    public void shouldUpdateDeliveredStockById() {
        Optional<Stock> stock = stockDao.getByPhoneId(phoneIds.get(1));
        Assert.isTrue(stock.isPresent(), ERROR_STOCK_NOT_FOUND);
        Assert.isTrue(stock.get().getReserved() == 1, ERROR_INCORRECT_RESERVED_AMOUNT);
        Assert.isTrue(stock.get().getStock() == 12, ERROR_INCORRECT_RESERVED_AMOUNT);

        Map<Long, Long> map = new HashMap<>();
        map.put(phoneIds.get(1), RESERVED_AMOUNT);
        Map<OrderStatus,Map<Long, Long>> mapForUpdate = new HashMap<>();
        mapForUpdate.put(OrderStatus.DELIVERED, map);
        stockDao.update(mapForUpdate);

        stock = stockDao.getByPhoneId(phoneIds.get(1));
        Assert.isTrue(stock.isPresent(), ERROR_STOCK_NOT_FOUND);
        Assert.isTrue(stock.get().getReserved() == 1 - RESERVED_AMOUNT, ERROR_INCORRECT_RESERVED_AMOUNT);
        Assert.isTrue(stock.get().getStock() == 12 - RESERVED_AMOUNT, ERROR_INCORRECT_RESERVED_AMOUNT);
    }

    @Test
    @DirtiesContext
    public void shouldUpdateRejectedStockById() {
        Optional<Stock> stock = stockDao.getByPhoneId(phoneIds.get(1));
        Assert.isTrue(stock.isPresent(), ERROR_STOCK_NOT_FOUND);
        Assert.isTrue(stock.get().getReserved() == 1, ERROR_INCORRECT_RESERVED_AMOUNT);
        Assert.isTrue(stock.get().getStock() == 12, ERROR_INCORRECT_RESERVED_AMOUNT);

        Map<Long, Long> map = new HashMap<>();
        map.put(phoneIds.get(1), RESERVED_AMOUNT);
        Map<OrderStatus,Map<Long, Long>> mapForUpdate = new HashMap<>();
        mapForUpdate.put(OrderStatus.REJECTED, map);
        stockDao.update(mapForUpdate);

        stock = stockDao.getByPhoneId(phoneIds.get(1));
        Assert.isTrue(stock.isPresent(), ERROR_STOCK_NOT_FOUND);
        Assert.isTrue(stock.get().getReserved() == 1 - RESERVED_AMOUNT, ERROR_INCORRECT_RESERVED_AMOUNT);
        Assert.isTrue(stock.get().getStock() == 12, ERROR_INCORRECT_RESERVED_AMOUNT);
    }

    @Test(expected = OutOfStockException.class)
    @DirtiesContext
    public void shouldNotUpdateStockByNotExistingId() {
        Optional<Stock> stock = stockDao.getByPhoneId(NOT_EXISTING_ID);
        Assert.isTrue(!stock.isPresent(), ERROR_STOCK_FOUND);

        stockDao.updateNew(NOT_EXISTING_ID, RESERVED_AMOUNT);

        stock = stockDao.getByPhoneId(NOT_EXISTING_ID);
        Assert.isTrue(!stock.isPresent(), ERROR_STOCK_FOUND);
    }


}
