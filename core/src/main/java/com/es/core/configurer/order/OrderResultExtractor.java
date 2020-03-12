package com.es.core.configurer.order;

import com.es.core.configurer.phone.PhoneResultExtractor;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import static com.es.core.constants.FieldConstants.*;

public class OrderResultExtractor extends PhoneResultExtractor {

    protected Order readPropertiesToOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setSubtotal(resultSet.getBigDecimal(SUBTOTAL));
        order.setDeliveryPrice(resultSet.getBigDecimal(DELIVERY_PRICE));
        order.setFirstName(resultSet.getString(FIRST_NAME));
        order.setLastName(resultSet.getString(LAST_NAME));
        order.setDeliveryAddress(resultSet.getString(DELIVERY_ADDRESS));
        order.setContactPhoneNo(resultSet.getString(CONTACT_PHONE_NO));
        order.setAdditionalInfo(resultSet.getString(ADDITIONAL_INFO));
        order.setStatus(OrderStatus.valueOf(resultSet.getString(STATUS)));
        order.setOrderItems(new ArrayList<>());
        return order;
    }

    protected OrderItem readPropertiesToOrderItem(Order order, ResultSet resultSet) throws SQLException {
        OrderItem orderItem = new OrderItem();
        Phone phone = readPropertiesToPhone(resultSet);
        orderItem.setPhone(phone);
        orderItem.setId(resultSet.getLong(ORDER_ITEM_ID));
        orderItem.setOrder(order);
        orderItem.setId(resultSet.getLong(ORDER_ID));
        orderItem.setQuantity(resultSet.getLong(QUANTITY));
        return orderItem;
    }

    protected void addOrderItem(Order order, Map<Long, OrderItem> orderItemMap, ResultSet resultSet) throws SQLException {
        Long orderItemId = resultSet.getLong(ORDER_ITEM_ID);
        OrderItem orderItem = orderItemMap.get(orderItemId);
        if (orderItem==null) {
            orderItem = readPropertiesToOrderItem(order, resultSet);
            order.getOrderItems().add(orderItem);
            orderItemMap.put(orderItemId, orderItem);
        }
        addColor(orderItem.getPhone(), resultSet);
    }
}
