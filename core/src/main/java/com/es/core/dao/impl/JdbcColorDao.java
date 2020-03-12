package com.es.core.dao.impl;

import com.es.core.dao.ColorDao;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.preparer.color.ColorBatchPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcColorDao implements ColorDao {
    private static final String SELECT_ALL_COLORS = "select * from colors";
    private static final String INSERT_INTO_PHONE2COLOR = "insert into phone2color values (?, ?)";
    private final static String DELETE_COLORS_IN_PHONE2COLOR = "delete from phone2color where phoneId = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcColorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Color> getAll() {
        return jdbcTemplate.query(SELECT_ALL_COLORS, new BeanPropertyRowMapper<>(Color.class));
    }

    @Override
    public void save(Phone phone) {
        jdbcTemplate.batchUpdate(INSERT_INTO_PHONE2COLOR, new ColorBatchPreparedStatementSetter(phone));
    }

    @Override
    public void delete(Phone phone) {
        jdbcTemplate.update(DELETE_COLORS_IN_PHONE2COLOR, phone.getId());
    }
}
