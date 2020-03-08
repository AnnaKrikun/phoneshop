package com.es.core.model.phone;

public class Stock {
    private Phone phone;
    private long stock;
    private long reserved;

    public Stock() {
    }

    public Stock(Phone phone, Integer stock, Integer reserved) {
        this.phone = phone;
        this.stock = stock;
        this.reserved = reserved;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public long getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    public Long getPhoneId() {
        return phone.getId();
    }
}
