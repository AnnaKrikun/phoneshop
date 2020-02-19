package com.es.core.dao;

import com.es.core.model.phone.Stock;

import java.util.List;
import java.util.Optional;

public interface StockDao {
    List<Stock> getStocks(List<Long> phoneIds);
    List<Stock> getPositiveStocks(List<Long> phoneIds);
    Optional<Stock> getStockById(Long phoneId);
    void update(Long phoneId, int reserved);
}
