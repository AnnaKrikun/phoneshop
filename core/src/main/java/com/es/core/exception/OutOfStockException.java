package com.es.core.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException() {
    }

    public OutOfStockException(Throwable cause) {
        super(cause);
    }
}
