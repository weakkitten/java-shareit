package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.error.exception.BadEmailException;
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
    public UserDto createUser(@Valid @RequestBody UserDto user) {//Скорее всего перенести логику в сервис
        log.info("Начало операции по добавлению пользователя - " + user);
        log.info("Проверка email");
        if (!service.checkEmail(user.getEmail())){
            log.info("Такого email нет в списке используемых");
            return service.addUser(user);
        } else {
            throw new BadEmailException("Такой email уже используется");
        }
    }

    @PatchMapping("/{userId}")//Доработать
    public UserDto updateUser(@PathVariable int userId,@RequestBody UserDtoUpdate user) {
        return service.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void removeUser(@PathVariable int userId) {
        service.removeUser(userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        return service.getUser(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }
}
