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
    private StockDao stockDao;

    @Autowired
    public StockServiceImpl(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    @Override
    public List<Stock> getByPhoneIds(List<Long> phoneIds) {
        return stockDao.getByPhoneIds(phoneIds);
    }

    @Override
    public List<Stock> getPositiveByPhoneIds(List<Long> phoneIds) {
        return stockDao.getPositiveByPhoneIds(phoneIds);
    }

    @Override
    public Optional<Stock> getByPhoneId(Long phoneId) {
        return stockDao.getByPhoneId(phoneId);
    }

    @Override
    public void update(Long phoneId, Long reserved) {
        stockDao.updateNew(phoneId, reserved);
    }

    @Override
    public Long getAvailableStock(Long phoneId) {
        Optional<Stock> stock = getByPhoneId(phoneId);
        return stock.isPresent() ? stock.get().getStock() - stock.get().getReserved() : 0L;
    }
}
