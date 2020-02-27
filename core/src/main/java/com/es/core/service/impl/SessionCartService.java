package com.es.core.service.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.dao.StockDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.ProductNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.service.CartService;
import com.es.core.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SessionCartService extends CalculationServiceImpl implements CartService {
    private Cart cart;
    private final PhoneDao phoneDao;
    private final StockDao stockDao;
    private final StockService stockService;

    @Autowired
    public SessionCartService(Cart cart, PhoneDao phoneDao, StockDao stockDao, StockService stockService) {
        this.cart = cart;
        this.phoneDao = phoneDao;
        this.stockDao = stockDao;
        this.stockService = stockService;
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        Phone phoneToAdd = checkPhone(phoneId);
        Stock phoneStock = checkStock(phoneId);
        addCartItem(phoneToAdd, quantity, phoneStock);
        recalculateTotals();
    }

    private void addCartItem(Phone phoneToAdd, Long quantity, Stock phoneStock) {
        Integer available = phoneStock.getStock() - phoneStock.getReserved();
        Optional<CartItem> optionalCartItem = findOptionalCartItem(phoneToAdd.getId());
        if (optionalCartItem.isPresent()) {
            updateCartItem(optionalCartItem.get(), quantity, available);
        } else {
            addNewCartItem(phoneToAdd, quantity, available);
        }
    }

    private void addNewCartItem(Phone phoneToAdd, Long quantity, Integer available) {
        if (quantity > available) {
            throw new OutOfStockException();
        }
        cart.getCartItems().add(new CartItem(phoneToAdd, quantity));
    }

    private void updateCartItem(CartItem cartItem, Long quantity, Integer available) {
        Long newQuantity = cartItem.getQuantity() + quantity;
        if (quantity > available) {
            throw new OutOfStockException();
        }
        cartItem.setQuantity(newQuantity);
    }

    private Stock checkStock(Long phoneId) {
        Optional<Stock> phoneStock = stockDao.getStockById(phoneId);
        if (!phoneStock.isPresent()) {
            throw new ProductNotFoundException(phoneId);
        }
        return phoneStock.get();
    }

    private Optional<CartItem> findOptionalCartItem(Long phoneId) {
        return cart.getCartItems().stream()
                .filter(item -> item.getPhone().getId().equals(phoneId))
                .findFirst();
    }

    @Override
    public void update(Map<Long, Long> items) {
        for (Map.Entry<Long, Long> item : items.entrySet()) {
            update(item.getKey(), item.getValue());
        }
    }

    @Override
    public void update(Long phoneId, Long newQuantity) {
        if (newQuantity.longValue() > 0) {
            Optional<CartItem> cartItemOptional = findOptionalCartItem(phoneId);
            if (cartItemOptional.isPresent()) {
                CartItem cartItem = cartItemOptional.get();
                cartItem.setQuantity(newQuantity);
            }
        } else {
            remove(phoneId);
        }
        recalculateTotals();
    }

    @Override
    public void remove(Long phoneId) {
        Optional<CartItem> cartItemToDelete = findOptionalCartItem(phoneId);
        if (cartItemToDelete.isPresent()) {
            cart.getCartItems().remove(cartItemToDelete.get());
            recalculateTotals();
        }
    }

    @Override
    public void clearCart() {
        List<Long> phoneIds = getCart().getCartItems().stream()
                .map(cartItem -> cartItem.getPhone().getId())
                .collect(Collectors.toList());
        phoneIds.forEach(this::remove);
    }

    @Override
    public void deleteOutOfStock() {
        List<CartItem> cartItems = cart.getCartItems();
        cartItems.stream().forEach(cartItem -> {
            Integer availableStock = stockService.getAvailableStock(cartItem.getPhone().getId());
            if (availableStock < cartItem.getQuantity()) {
                remove(cartItem.getPhone().getId());
            }
        });
    }

    @Override
    public BigDecimal getCartTotalPrice() {
        return getCart().getTotalPrice();
    }

    @Override
    public Long getCartTotalQuantity() {
        return getCart().getTotalQuantity();
    }

    private Phone checkPhone(Long phoneId) throws ProductNotFoundException {
        Optional<Phone> phone = phoneDao.get(phoneId);
        if (!phone.isPresent()) {
            throw new ProductNotFoundException(phoneId);
        }
        Phone phoneToAdd = phone.get();
        if (phoneToAdd.getPrice() == null) {
            throw new IllegalArgumentException();
        }
        return phoneToAdd;
    }
}
