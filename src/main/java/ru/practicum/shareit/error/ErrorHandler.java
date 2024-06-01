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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExceptions(final NotFoundException e) {
        log.debug("Объект не найден");
        return new ErrorResponse("В процессе поиска возникла ошибки", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        log.debug("Данного значение нет в памяти или БД");
        return new ErrorResponse("Во время запроса произошла ошибка", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleBadEmailException(final BadEmailException e) {
        log.debug("Почта уже есть в списке");
        return new ErrorResponse("Такая почта уже используется", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleTimeException(final TimeException e) {
        log.debug("Время в аренде некорректно");
        return new ErrorResponse("Время некорректно", e.getMessage());
    }

/*    @ExceptionHandler//Перекрывает handleBadEmailException
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleBadEmailException(final RuntimeException e) {
        log.debug("Ошибка в работе приложения");
        return new ErrorResponse("Неизвестная ошибка", e.getMessage());
    }*/
}
