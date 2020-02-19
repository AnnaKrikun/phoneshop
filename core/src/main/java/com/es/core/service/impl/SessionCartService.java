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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class SessionCartService implements CartService {
    @Resource
    private Cart cart;
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private StockDao stockDao;

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
            updateCartItem(optionalCartItem.get(), quantity, available, phoneStock);
        } else {
            addNewCartItem(phoneToAdd, quantity, available, phoneStock);
        }
    }

    private void addNewCartItem(Phone phoneToAdd, Long quantity, Integer available, Stock phoneStock) {
        if (quantity > available) {
            throw new OutOfStockException();
        }
        cart.getCartItems().add(new CartItem(phoneToAdd, quantity));
        stockDao.update(phoneToAdd.getId(), Math.toIntExact(quantity + phoneStock.getReserved()));
    }

    private void updateCartItem(CartItem cartItem, Long quantity, Integer available, Stock phoneStock) {
        Long newQuantity = cartItem.getQuantity() + quantity;
        if (quantity > available) {
            throw new OutOfStockException();
        }
        cartItem.setQuantity(newQuantity);
        stockDao.update(cartItem.getPhone().getId(), Math.toIntExact(newQuantity + phoneStock.getReserved()));
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
            Optional<CartItem> cartItem = findOptionalCartItem(item.getKey());
            if (cartItem.isPresent()) {
                update(item.getKey(), item.getValue());
            } else {
                addPhone(item.getKey(), item.getValue());
            }
        }
    }

    private void update(Long phoneId, Long quantity) {
        stockDao.update(phoneId, Math.toIntExact(quantity));
    }

    @Override
    public void remove(Long phoneId) {
        Optional<CartItem> cartItemToDelete = findOptionalCartItem(phoneId);
        if (cartItemToDelete.isPresent()) {
            cart.getCartItems().remove(cartItemToDelete.get());
            recalculateTotals();
            Optional<Stock> stock = stockDao.getStockById(phoneId);
            stockDao.update(phoneId, Math.toIntExact(stock.get().getReserved() -
                    cartItemToDelete.get().getQuantity()));
        }
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

    private void recalculateTotals() {
        cart.setTotalQuantity(cart.getCartItems().stream()
                .mapToLong(CartItem::getQuantity)
                .sum());
        cart.setTotalPrice(cart.getCartItems().stream()
                .map(item -> item.getPhone().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
