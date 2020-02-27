package com.es.core.trigger;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderItemsTrigger implements Trigger {
    private static final String UPDATE_STOCK = "update stocks set reserved = reserved + ? "
    +" where phoneId = ? and stock >= reserved + ?";

    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {
    }

    @Override
    public void fire(Connection connection, Object[] oldRows, Object[] newRows) throws SQLException {
        Long phoneId = getPhoneId(oldRows, newRows);
        Long quantity = getQuantity(oldRows, newRows);
        if (phoneId == null) {
            throw new SQLException();
        }
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STOCK);
        preparedStatement.setLong(1, quantity);
        preparedStatement.setLong(2, phoneId);
        preparedStatement.setLong(3, quantity);
        if (preparedStatement.executeUpdate() == 0) {
            throw new SQLException();
        }
    }

    private Long getQuantity(Object[] oldRows, Object[] newRows) {
        Long oldQuantity = 0L;
        Long newQuantity = 0L;
        if (oldRows[3] != null) {
            oldQuantity = (Long) oldRows[3];
        }
        if (newRows[3] != null) {
            newQuantity = (Long) newRows[3];
        }
        return newQuantity - oldQuantity;
    }

    private Long getPhoneId(Object[] oldRows, Object[] newRows) {
        if (oldRows[2] != null) {
            return (Long) oldRows[2];
        } else if (newRows[2] != null) {
            return (Long) newRows[2];
        }
        return null;
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public void remove() throws SQLException {

    }
}
