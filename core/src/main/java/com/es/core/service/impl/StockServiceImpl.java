package com.es.core.service.impl;

import com.es.core.dao.StockDao;
import com.es.core.model.phone.Stock;
import com.es.core.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {
    private StockDao stockDao;

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
    public Optional<Stock> getStockById(Long phoneId) { return stockDao.getStockById(phoneId); }

    @Override
    public void update(Long phoneId, Long reserved) {
        stockDao.updateNew(phoneId, reserved);
    }

    @Override
    public Long getAvailableStock(Long phoneId) {
        Optional<Stock> stock = getStockById(phoneId);
        return stock.isPresent() ? (long)(stock.get().getStock() - stock.get().getReserved()) : 0L;
    }
}
