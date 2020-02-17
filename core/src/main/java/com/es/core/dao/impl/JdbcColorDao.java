package com.es.core.dao.impl;

import com.es.core.dao.ColorDao;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public List<Color> getAllColors() {
        return jdbcTemplate.query(SELECT_ALL_COLORS, new BeanPropertyRowMapper<>(Color.class));
    }

    @Override
    public void saveColor(Phone phone) {
        jdbcTemplate.batchUpdate(INSERT_INTO_PHONE2COLOR, new BatchPreparedStatementSetter() {
            List<Color> colors = new ArrayList<>(phone.getColors());

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, phone.getId());
                preparedStatement.setLong(2, colors.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return colors.size();
            }
        });
    }

    @Override
    public void deleteColorsFromPone2Color(Phone phone) {
        jdbcTemplate.update(DELETE_COLORS_IN_PHONE2COLOR, phone.getId());
    }
}
