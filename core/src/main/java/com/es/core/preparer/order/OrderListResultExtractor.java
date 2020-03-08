package com.es.core.preparer.order;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.es.core.constants.FieldConstants.ORDER_ID;

@Component
public class OrderListResultExtractor extends OrderResultExtractor implements ResultSetExtractor<List<Order>> {
    @Override
    public List<Order> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Long, Order> orderMap = new HashMap<>();
        Map<Long, OrderItem> orderItemMap = new HashMap<>();
        List<Order> orderList = new ArrayList<>();

        while (resultSet.next()) {
            Long orderId = resultSet.getLong(ORDER_ID);
            Order changeOrder = orderMap.get(orderId);
            if (changeOrder == null) {
                changeOrder = readPropertiesToOrder(resultSet);
                orderMap.put(orderId, changeOrder);
                orderList.add(changeOrder);
            }
            addOrderItem(changeOrder, orderItemMap, resultSet);
        }
        return orderList;
    }
}
