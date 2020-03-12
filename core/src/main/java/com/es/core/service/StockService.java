package com.es.core.service;

import com.es.core.model.phone.Stock;

import java.util.List;
import java.util.Optional;

public interface StockService {
    List<Stock> getByPhoneIds(List<Long> phoneIds);

    List<Stock> getPositiveByPhoneIds(List<Long> phoneIds);

    Optional<Stock> getByPhoneId(Long phoneId);

    void update(Long phoneId, Long reserved);

    Long getAvailableStock(Long phoneId);
}
