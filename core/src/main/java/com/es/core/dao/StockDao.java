package com.es.core.dao;

import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Stock;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StockDao {
    List<Stock> getByPhoneIds(List<Long> phoneIds);

    List<Stock> getPositiveByPhoneIds(List<Long> phoneIds);

    Optional<Stock> getByPhoneId(Long phoneId);

    void updateNew(Long phoneId, Long reserved);

    void update(Map<OrderStatus, Map<Long, Long>> map);
}
