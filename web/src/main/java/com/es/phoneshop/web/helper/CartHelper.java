package com.es.phoneshop.web.helper;

import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.service.CartService;
import com.es.phoneshop.web.cart.CartDisplay;
import com.es.phoneshop.web.cart.CartItemDisplay;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CartHelper {
    private static final String CART_DISPLAY = "cartDisplay";
    private static final String CART = "cart";

    @Resource
    private CartService cartService;

    public Map<Long, Long> createMapForUpdate(CartDisplay cartDisplay) {
        Map<Long, Long> mapForUpdate = cartDisplay.getCartDisplayItems().stream()
                .collect(Collectors.toMap(CartItemDisplay::getPhoneId, CartItemDisplay::getQuantity));
        return mapForUpdate;

    }

    public void setCartDisplayAttribute(Model model) {
        Cart cart = cartService.getCart();
        List<CartItemDisplay> cartDisplayItems = cart.getCartItems().stream()
                .map(item -> new CartItemDisplay(item.getPhone(), item.getQuantity()))
                .collect(Collectors.toList());

        model.addAttribute(CART_DISPLAY, new CartDisplay(cartDisplayItems));
        model.addAttribute(CART, cartService.getCart());
    }

    public void setPropertiesCartUpdateDisplay(CartDisplay cartDisplay, Model model) {
        Cart cart = cartService.getCart();
        Map<Long, CartItem> items = cart.getCartItems().stream()
                .collect(Collectors.toMap(item -> item.getPhone().getId(), item -> item));

        cartDisplay.getCartDisplayItems()
                .forEach(cartDisplayItem ->
                        setPropertiesCartItem(cartDisplayItem, items.get(cartDisplayItem.getPhoneId())));
        model.addAttribute(CART, cartService.getCart());
    }

    private void setPropertiesCartItem(CartItemDisplay cartDisplayItem, CartItem cartItem) {
        Phone phone = cartItem.getPhone();
        if (phone != null) {
            cartDisplayItem.setBrand(phone.getBrand());
            cartDisplayItem.setImageUrl(phone.getImageUrl());
            cartDisplayItem.setModel(phone.getModel());
            cartDisplayItem.setPrice(phone.getPrice());
            cartDisplayItem.setDisplaySizeInches(phone.getDisplaySizeInches());
            cartDisplayItem.setColors(phone.getColors());
        }
    }
}
