package com.es.phoneshop.web.cart;

public class CartItemStringData {
    private String phoneIdString;
    private String quantityString;

    public CartItemStringData() {
    }

    public CartItemStringData(String phoneIdString, String quantityString) {
        this.phoneIdString = phoneIdString;
        this.quantityString = quantityString;
    }

    public String getPhoneIdString() {
        return phoneIdString;
    }

    public void setPhoneIdString(String phoneIdString) {
        this.phoneIdString = phoneIdString;
    }

    public String getQuantityString() {
        return quantityString;
    }

    public void setQuantityString(String quantityString) {
        this.quantityString = quantityString;
    }
}
