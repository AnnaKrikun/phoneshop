package com.es.phoneshop.web.cart;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class CartDisplay implements Serializable {
    @Valid
    @NotNull
    private List<CartItemDisplay> cartDisplayItems;

    public CartDisplay() {
    }

    public CartDisplay(List<CartItemDisplay> cartDisplayItems) {
        this.cartDisplayItems = cartDisplayItems;
    }

    public List<CartItemDisplay> getCartDisplayItems() {
        return cartDisplayItems;
    }

    public void setCartDisplayItems(List<CartItemDisplay> cartDisplayItems) {
        this.cartDisplayItems = cartDisplayItems;
    }
}
