package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto user) {
        log.info("Начало операции по добавлению пользователя - " + user);
        log.info("Проверка email");
        return service.addUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable int userId,@RequestBody UserDtoUpdate user) {
        log.info("Начало операции по обновлению пользователя - " + user);
        return service.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable int userId) {
        log.info("Начало операции по удалению пользователя с id- " + userId);
        service.removeUser(userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        log.info("Начало операции по поиску пользователя с id - " + userId);
        return service.getUser(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Начало операции по выгрузке всех пользователей");
        return service.getAllUsers();
    }
}
