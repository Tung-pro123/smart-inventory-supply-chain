package com.tung.inventory.exception;

public class InsufficientStockException extends RuntimeException {
    private final int requested;
    private final int available;

    public InsufficientStockException(int requested, int available) {
        super(String.format("Insufficient stock. Requested: %d, Available: %d", requested, available));
        this.requested = requested;
        this.available = available;
    }

    public InsufficientStockException(String message) {
        super(message);
        this.requested = 0;
        this.available = 0;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }
}
