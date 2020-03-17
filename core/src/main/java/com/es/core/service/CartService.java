package com.es.core.service;

import com.es.core.model.cart.Cart;

import java.math.BigDecimal;
import java.util.Map;

public interface CartService {

    Cart getCart();

    void addPhone(Long phoneId, Long quantity);

    void addPhones(Map<Long, Long> items);

    /**
     * @param items key: {@link com.es.core.model.phone.Phone#id}
     *              value: quantity
     */
    void update(Map<Long, Long> items);

    void update(Long phoneId, Long quantity);

    void remove(Long phoneId);

    void clearCart();

    void deleteOutOfStock();

    BigDecimal getCartTotalPrice();

    Long getCartTotalQuantity();
}
