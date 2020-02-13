package com.es.core.dao.impl;

import com.es.core.dao.StockDao;
import com.es.core.model.phone.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class JdbcStockDao implements StockDao {
    private static final String SELECT_STOCKS_BY_ID = "select * from stocks where phoneId in (:phoneIds)";
    private static final String SELECT_POSITIVE_STOCKS_BY_ID = "select * from stocks where phoneId in (:phoneIds) "+
            " and stock > 0";
    private static final String SELECT_STOCK_BY_ID = "select * from stocks where phoneId = ? "+
            " and stock > 0";
    private static final String UPDATE_STOCK_BY_ID = "update stocks set reserved = ? where phoneId = ?";
    private static final String PHONE_IDS = "phoneIds";

    private final JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcStockDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public List<Stock> getStocks(List<Long> phoneIds) {
        Map<String, List<Long>> phoneIdsMap = new HashMap<>();
        phoneIdsMap.put(PHONE_IDS, phoneIds);
        return namedParameterJdbcTemplate.query(SELECT_STOCKS_BY_ID, phoneIdsMap,
                new BeanPropertyRowMapper<>(Stock.class));
    }

    @Override
    public List<Stock> getPositiveStocks(List<Long> phoneIds) {
        Map<String, List<Long>> phoneIdsMap = new HashMap<>();
        phoneIdsMap.put(PHONE_IDS, phoneIds);
        return namedParameterJdbcTemplate.query(SELECT_POSITIVE_STOCKS_BY_ID, phoneIdsMap,
                new BeanPropertyRowMapper<>(Stock.class));
    }

    @Override
    public Optional<Stock> getStockById(Long phoneId) {
        List<Stock> stocks = jdbcTemplate.query(SELECT_STOCK_BY_ID, new Object[]{phoneId},
                new BeanPropertyRowMapper<>(Stock.class));
        if (stocks!=null && !stocks.isEmpty()){
            return Optional.of(stocks.get(0));
        }
        return Optional.empty();
    }

    @Override
    public void update(Long phoneId, int reserved) {
        jdbcTemplate.update(UPDATE_STOCK_BY_ID, new Object[]{reserved, phoneId});
    }
}
