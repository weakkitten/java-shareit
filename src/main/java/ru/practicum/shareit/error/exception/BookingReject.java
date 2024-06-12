package ru.practicum.shareit.error.exception;

public class BookingReject extends RuntimeException {
    public BookingReject(String message) {
        super(message);
    }
}
