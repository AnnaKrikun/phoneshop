package com.es.core.service;

import com.es.core.model.phone.Stock;

import java.util.List;
import java.util.Optional;

public interface StockService {
    List<Stock> getStocks(List<Long> phoneIds);
    List<Stock> getPositiveStocks(List<Long> phoneIds);
    Optional<Stock> getStockById(Long phoneId);
    void update(Long phoneId, int reserved);
    Integer getAvailableStock(Stock stock);
}
