package com.es.core.service;

import com.es.core.model.cart.Cart;

import java.math.BigDecimal;
import java.util.Map;

public interface CartService {

    Cart getCart();

    void addPhone(Long phoneId, Long quantity) throws Exception;

    /**
     * @param items key: {@link com.es.core.model.phone.Phone#id}
     *              value: quantity
     */
    void update(Map<Long, Long> items) throws Exception;

    void remove(Long phoneId);

    BigDecimal getCartTotalPrice();

    Long getCartTotalQuantity();
}
