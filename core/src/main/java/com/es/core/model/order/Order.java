package com.es.core.model.order;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.List;

public class Order {
    private Long id;
    private List<OrderItem> orderItems;
    /**
     * A sum of order item prices;
     */
    private BigDecimal subtotal;
    private BigDecimal deliveryPrice;
    /**
     * <code>subtotal</code> + <code>deliveryPrice</code>
     */
    private BigDecimal totalPrice;

    @NotEmpty(message = "First name is required!")
    @Pattern(regexp = "[a-zA-Z]{0,40}", message = "Wrong format!")
    private String firstName;

    @NotEmpty(message = "Last name is required!")
    @Pattern(regexp = "[a-zA-Z]{0,40}", message = "Wrong format!")
    private String lastName;

    @NotEmpty(message = "Address is required!")
    @Length(max = 200, message = "Wrong format!")
    private String deliveryAddress;

    @NotEmpty(message = "Contact phone number is required!")
    @Pattern(regexp = "(\\+*)\\d{7,12}", message = "Wrong format!")
    private String contactPhoneNo;

    @Length(max = 5000, message = "Too long info!")
    private String additionalInfo;

    private OrderStatus status;
    private Date date;

    public Order() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
