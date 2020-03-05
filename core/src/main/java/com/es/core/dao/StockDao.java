package com.es.core.dao;

import com.es.core.model.phone.Stock;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StockDao {
    List<Stock> getStocks(List<Long> phoneIds);
    List<Stock> getPositiveStocks(List<Long> phoneIds);
    Optional<Stock> getStockById(Long phoneId);
    void updateNew(Long phoneId, Long reserved);
    void updateDelivered(Map<Long, Long> map);
    void updateRejected(Map<Long, Long> map);
}
