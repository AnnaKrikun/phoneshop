package com.es.core.preparer.order;

import com.es.core.model.order.OrderItem;
import com.es.core.preparer.ParametersPreparer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.es.core.constants.FieldConstants.*;

@Component
public class OrderItemParametersPreparer implements ParametersPreparer<OrderItem> {
    @Override
    public Object[] getPreparedParameters(OrderItem orderItem) {
        Object[] preparedParameters = new Object[ORDER_ITEM_FIELD_AMOUNT];
        preparedParameters[0] = orderItem.getPhone().getId();
        preparedParameters[1] = orderItem.getOrder().getId();
        preparedParameters[2] = orderItem.getQuantity();
        preparedParameters[3] = orderItem.getId();
        return preparedParameters;
    }

    @Override
    public Map<String, Object> fillMapForSaving(OrderItem orderItem) {
        Map<String, Object> map = new HashMap<>();
        map.put(PHONE_ID, orderItem.getPhone().getId());
        map.put(ORDER_ID, orderItem.getOrder().getId());
        map.put(QUANTITY, orderItem.getQuantity());
        map.put(ORDER_ITEM_ID, orderItem.getId());
        return map;
    }
}
