package com.es.phoneshop.web.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuickProductEntry {
    private List<QuickProductEntryItem> quickProductEntryItems;
    private Map<Long, Long> validData = new HashMap<>();

    public QuickProductEntry() {
    }

    public QuickProductEntry(List<QuickProductEntryItem> quickProductEntryItems) {
        this.quickProductEntryItems = quickProductEntryItems;
    }

    public List<QuickProductEntryItem> getQuickProductEntryItems() {
        return quickProductEntryItems;
    }

    public void setQuickProductEntryItems(List<QuickProductEntryItem> quickProductEntryItems) {
        this.quickProductEntryItems = quickProductEntryItems;
    }

    public Map<Long, Long> getValidData() {
        return validData;
    }

    public void setValidData(Map<Long, Long> validData) {
        this.validData = validData;
    }
}
