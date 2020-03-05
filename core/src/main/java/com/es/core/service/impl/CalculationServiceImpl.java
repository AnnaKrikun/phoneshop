package com.es.core.service.impl;

import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class CalculationServiceImpl implements CalculationService {
    @Autowired
    private Cart cart;

    @Override
    public void recalculateTotals() {
        recalculateTotalQuantity();
        recalculateTotalPrice();
    }

    @Override
    public void recalculateTotalPrice() {
        cart.setTotalPrice(cart.getCartItems().stream()
                .map(item -> item.getPhone().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    @Override
    public void recalculateTotalQuantity() {
        cart.setTotalQuantity(cart.getCartItems().stream()
                .mapToLong(CartItem::getQuantity)
                .sum());
    }
}
