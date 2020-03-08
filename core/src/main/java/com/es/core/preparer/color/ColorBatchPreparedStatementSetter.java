package com.es.core.preparer.color;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColorBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
    private Phone phone;
    List<Color> colors;

    public ColorBatchPreparedStatementSetter(Phone phone) {
        this.phone = phone;
        colors = new ArrayList<>(phone.getColors());
    }

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        preparedStatement.setLong(1, phone.getId());
        preparedStatement.setLong(2, colors.get(i).getId());
    }

    @Override
    public int getBatchSize() {
        return colors.size();
    }
}
