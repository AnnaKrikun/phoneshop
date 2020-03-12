package com.es.core.service;

import com.es.core.dao.StockDao;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.service.impl.StockServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceImplTest {
    private static final Integer STOCK = 10;
    private static final Integer RESERVED = 1;
    private static final Long EXPECTED_AVAILABLE_STOCK = 9L;
    private static final String ERROR = "Error";
    private static final Long EXPECTED_ZERO_AVAILABLE_STOCK = 0L;
    @Mock
    private StockDao mockStockDao;

    @InjectMocks
    private StockService stockService = new StockServiceImpl(mockStockDao);

    private List<Long> phoneIds;
    private List<Phone> phoneList;
    private List<Stock> stockList;

    @Before
    public void init() {
        initPhoneList();
        initStockList();
        initMockStockDao();
    }

    @Test
    public void shouldGetAvailableStockCorrectly(){
        Long availableStock = stockService.getAvailableStock(phoneList.get(0).getId());

        verify(mockStockDao).getByPhoneId(phoneList.get(0).getId());
        Assert.isTrue(availableStock.equals(EXPECTED_AVAILABLE_STOCK), ERROR);
    }

    @Test
    public void shouldGetAvailableStockOfNotExistingCorrectly(){
        Long availableStock = stockService.getAvailableStock(100L);

        verify(mockStockDao).getByPhoneId(100L);
        Assert.isTrue(availableStock.equals(EXPECTED_ZERO_AVAILABLE_STOCK), ERROR);
    }

    @Test
    public void shouldGetStocksCorrectly(){
        List<Stock> stocks = stockService.getByPhoneIds(phoneIds);

        verify(mockStockDao).getByPhoneIds(phoneIds);
        Assert.isTrue(stocks.size() == phoneIds.size(), ERROR);
    }

    @Test
    public void shouldGetPositiveStocksCorrectly(){
        List<Stock> stocks = stockService.getPositiveByPhoneIds(phoneIds);

        verify(mockStockDao).getPositiveByPhoneIds(phoneIds);
        Assert.isTrue(stocks.size() == phoneIds.size(), ERROR);
    }

    @Test
    public void shouldUpdateStockCorrectly(){
        stockService.update(phoneList.get(0).getId(), 2L);

        verify(mockStockDao).updateNew(phoneList.get(0).getId(), 2L);
    }

    private void initStockList() {
        stockList = new ArrayList<>();
        phoneList.stream().forEach(phone -> stockList.add(new Stock(phone, STOCK, RESERVED)));
    }

    private void initMockStockDao() {
        when(mockStockDao.getByPhoneId(isA(Long.class))).thenReturn(Optional.empty());
        for (Stock stock : stockList) {
            when(mockStockDao.getByPhoneId(stock.getPhone().getId())).thenReturn(Optional.of(stock));
        }
        when(mockStockDao.getByPhoneIds(phoneIds)).thenReturn(stockList);
        when(mockStockDao.getPositiveByPhoneIds(phoneIds)).thenReturn(stockList);
    }

    private void initPhoneList() {
        phoneList = new ArrayList<>();
        phoneIds = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            phoneList.add(createPhone(Integer.toString(i), Integer.toString(i), (long) i));
            phoneIds.add((long) i);
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
}
