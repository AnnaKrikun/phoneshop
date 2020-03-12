package com.es.phoneshop.web.exception;

public class PageNotFoundException extends RuntimeException {
    private Long id;

    public PageNotFoundException() {
    }

    public PageNotFoundException(Long phoneId) {
        this.id = phoneId;
    }

    public PageNotFoundException(String message, Long phoneId) {
        super(message);
        this.id = phoneId;
    }

    public Long getId() {
        return id;
    }
}
