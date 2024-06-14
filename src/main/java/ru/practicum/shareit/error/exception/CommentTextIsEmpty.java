package ru.practicum.shareit.error.exception;

public class CommentTextIsEmpty extends RuntimeException {
    public CommentTextIsEmpty(String message) {
        super(message);
    }
}
