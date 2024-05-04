package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.error.exception.BadEmailException;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Начало операции по добавлению пользователя - " + user);
        log.info("Проверка email");
        if (!service.checkEmail(user.getEmail())) {
            log.info("Такого email нет в списке используемых");
            return service.addUser(user);
        }else {
            throw new BadEmailException("Такой email уже используется");
        }
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable int userId,@Valid @RequestBody User user) {
        return service.updateUser(userId, user);
    }
}
