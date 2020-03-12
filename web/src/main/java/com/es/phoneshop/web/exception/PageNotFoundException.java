package com.es.phoneshop.web.exception;

public class PageNotFoundException extends RuntimeException {
    private Long phoneId;

    public PageNotFoundException() {
    }

    public PageNotFoundException(Long phoneId) {
        this.phoneId = phoneId;
    }

    public PageNotFoundException(String message, Long phoneId) {
        super(message);
        this.phoneId = phoneId;
    }

    public Long getPhoneId() {
        return phoneId;
    }
}
