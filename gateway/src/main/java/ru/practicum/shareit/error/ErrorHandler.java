package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.error.exception.*;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTimeException(final TimeException e) {
        log.debug("Время в аренде некорректно");
        return new ErrorResponse("Время некорректно", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnsupportedState(final UnsupportedState e) {
        log.debug("State некорректен");
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCommentTextIsEmpty(final CommentTextIsEmpty e) {
        log.debug("Запрос отклонен");
        return new ErrorResponse("Некорректный текст", e.getMessage());
    }

/*    @ExceptionHandler//Перекрывает handleBadEmailException
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleBadEmailException(final RuntimeException e) {
        log.debug("Ошибка в работе приложения");
        return new ErrorResponse("Неизвестная ошибка", e.getMessage());
    }*/
}
