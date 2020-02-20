package com.es.phoneshop.web.cart;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

public class CartItemDisplay {
    @NotNull(message = "Phone id can't be empty!")
    @Min(value = 1L, message = "Phone id can't be less than 1!")
    private Long phoneId;

    @NotNull(message = "Quantity can't be empty!")
    @Min(value = 0L, message = "Quantity can't be less than zero!")
    private Long quantity = 0L;

    private String imageUrl;
    private String model;
    private String brand;
    private BigDecimal displaySizeInches = BigDecimal.ZERO;
    private BigDecimal price = BigDecimal.ZERO;
    private Set<Color> colors = Collections.EMPTY_SET;

    public CartItemDisplay() {
    }

    public CartItemDisplay(Phone phone, Long quantity) {
        this.phoneId = phone.getId();
        this.imageUrl = phone.getImageUrl();
        this.model = phone.getModel();
        this.brand = phone.getBrand();
        if (phone.getDisplaySizeInches() != null) {
            this.displaySizeInches = phone.getDisplaySizeInches();
        }
        if (phone.getPrice() != null) {
            this.price = phone.getPrice();
        }
        this.quantity = quantity;
        this.colors = phone.getColors();
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDisplaySizeInches() {
        return displaySizeInches;
    }

    public void setDisplaySizeInches(BigDecimal displaySizeInches) {
        this.displaySizeInches = displaySizeInches;
    }

    public Set<Color> getColors() {
        return colors;
    }

    public void setColors(Set<Color> colors) {
        this.colors = colors;
    }
}
