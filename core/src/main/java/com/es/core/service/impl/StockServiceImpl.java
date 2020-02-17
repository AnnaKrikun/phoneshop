package com.es.core.service.impl;

import com.es.core.dao.StockDao;
import com.es.core.model.phone.Stock;
import com.es.core.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {
    private final StockDao stockDao;

    @Autowired
    public StockServiceImpl(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    @Override
    public List<Stock> getStocks(List<Long> phoneIds) {
        return stockDao.getStocks(phoneIds);
    }

    @Override
    public List<Stock> getPositiveStocks(List<Long> phoneIds) {
        return stockDao.getPositiveStocks(phoneIds);
    }

    @Override
    public Optional<Stock> getStockById(Long phoneId) {
        return stockDao.getStockById(phoneId);
    }

    @Override
    public void update(Long phoneId, int reserved) {
        stockDao.update(phoneId, reserved);
    }

    @Override
    public Integer getAvailableStock(Stock stock) {
        return stock.getStock() - stock.getReserved();
    }
}
