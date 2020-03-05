package com.es.core.configurer.order;

import com.es.core.configurer.ParametersPreparer;
import com.es.core.model.order.Order;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.JDBCType;
import java.util.HashMap;
import java.util.Map;

import static com.es.core.constants.FieldConstants.*;

@Component
public class OrderParametersPreparer implements ParametersPreparer<Order> {
    @Override
    public Object[] getPreparedParameters(Order order) {
        Object[] preparedParameters = new Object[ORDER_FIELD_AMOUNT];
        preparedParameters[0] = order.getSubtotal();
        preparedParameters[1] = order.getDeliveryPrice();
        preparedParameters[2] = order.getTotalPrice();
        preparedParameters[3] = order.getFirstName();
        preparedParameters[4] = order.getLastName();
        preparedParameters[5] = order.getDeliveryAddress();
        preparedParameters[6] = order.getContactPhoneNo();
        preparedParameters[7] = order.getAdditionalInfo();
        preparedParameters[8] = order.getStatus();
        preparedParameters[9] = order.getId();
        return preparedParameters;
    }

    @Override
    public Map<String, Object> fillMapForSaving(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put(SUBTOTAL, order.getSubtotal());
        map.put(DELIVERY_PRICE, order.getDeliveryPrice());
        map.put(TOTAL_PRICE, order.getTotalPrice());
        map.put(FIRST_NAME, order.getFirstName());
        map.put(LAST_NAME, order.getLastName());
        map.put(DELIVERY_ADDRESS, order.getDeliveryAddress());
        map.put(CONTACT_PHONE_NO, order.getContactPhoneNo());
        map.put(ADDITIONAL_INFO, order.getAdditionalInfo());
        map.put(STATUS, order.getStatus().name());
        return map;
    }

    public SqlParameterSource fillMapForSavingSQL(Order order) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(SUBTOTAL, order.getSubtotal());
        mapSqlParameterSource.addValue(DELIVERY_PRICE, order.getDeliveryPrice());
        mapSqlParameterSource.addValue(TOTAL_PRICE, order.getTotalPrice());
        mapSqlParameterSource.addValue(FIRST_NAME, order.getFirstName());
        mapSqlParameterSource.addValue(LAST_NAME, order.getLastName());
        mapSqlParameterSource.addValue(DELIVERY_ADDRESS, order.getDeliveryAddress());
        mapSqlParameterSource.addValue(CONTACT_PHONE_NO, order.getContactPhoneNo());
        mapSqlParameterSource.addValue(ADDITIONAL_INFO, order.getAdditionalInfo());
        mapSqlParameterSource.addValue(DATE, order.getDate(), JDBCType.DATE.getVendorTypeNumber());
        mapSqlParameterSource.addValue(STATUS, order.getStatus().name(), 12);
        return mapSqlParameterSource;
    }
}
