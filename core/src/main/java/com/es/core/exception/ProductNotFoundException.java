package com.es.core.exception;

public class ProductNotFoundException extends RuntimeException {
    private Long id;

    public ProductNotFoundException(){};

    public ProductNotFoundException(Long id) {
        this.id = id;
    }

    public ProductNotFoundException(String message, Long i) {
        super(message);
        id = i;
    }

    public Long getId() {
        return id;
    }

}
