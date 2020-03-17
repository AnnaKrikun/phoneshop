package com.es.phoneshop.web.cart;

public class QuickProductEntryItem {
    private String phoneId;
    private Long phoneQuantity = 0L;

    public QuickProductEntryItem() {
    }

    public QuickProductEntryItem(String phoneId, Long phoneQuantity) {
        this.phoneId = phoneId;
        this.phoneQuantity = phoneQuantity;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public Long getPhoneQuantity() {
        return phoneQuantity;
    }

    public void setPhoneQuantity(Long phoneQuantity) {
        this.phoneQuantity = phoneQuantity;
    }
}
