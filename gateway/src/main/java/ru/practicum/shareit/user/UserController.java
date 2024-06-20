package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.Client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserClient service;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto user) {
        log.info("Начало операции по добавлению пользователя - " + user);
        log.info("Проверка email");
        return service.createUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable int userId,@RequestBody UserDtoUpdate user) {
        log.info("Начало операции по обновлению пользователя - " + user);
        return service.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable int userId) {
        log.info("Начало операции по удалению пользователя с id- " + userId);
        service.removeUser(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable int userId) {
        log.info("Начало операции по поиску пользователя с id - " + userId);
        return service.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Начало операции по выгрузке всех пользователей");
        return service.getAllUsers();
    }
}
