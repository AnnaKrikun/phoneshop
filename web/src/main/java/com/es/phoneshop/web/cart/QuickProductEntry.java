package com.es.phoneshop.web.cart;

import java.util.HashMap;
import java.util.Map;

public class QuickProductEntry {
    private String[] phoneIds;
    private String[] phoneQuantities;
    Map<Long,Long> validData = new HashMap<>();

    public QuickProductEntry() {
    }

    public String[] getPhoneIds() {
        return phoneIds;
    }

    public void setPhoneIds(String[] phoneIds) {
        this.phoneIds = phoneIds;
    }

    public String[] getPhoneQuantities() {
        return phoneQuantities;
    }

    public void setPhoneQuantities(String[] phoneQuantities) {
        this.phoneQuantities = phoneQuantities;
    }

    public Map<Long, Long> getValidData() {
        return validData;
    }

    public void setValidData(Map<Long, Long> validData) {
        this.validData = validData;
    }
}
