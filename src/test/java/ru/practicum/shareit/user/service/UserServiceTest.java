package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void getUserReturnException() {
        assertThrows(NotFoundException.class, () -> userService.getUser(10));
    }

    @Test
    void getUserReturnUser() {
        User user = User.builder()
                .id(2)
                .name("Имя")
                .email("Почта")
                .build();
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                        .thenReturn(Optional.ofNullable(user));
        assertEquals(userService.getUser(2), UserMapper.toUserDto(user));
        Mockito.verify(userRepository, Mockito.times(2)).findById(2);
    }

    @Test
    void addUser() {
        User user = User.builder()
                .name("Имя")
                .email("Почта")
                .build();
        UserDto dto = UserMapper.toUserDto(user);
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        assertEquals(userService.addUser(UserMapper.toUserDto(user)), dto);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void removeUser() {
        assertEquals(userService.removeUser(15), true);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(15);
    }

    @Test
    void updateUser() {
        User user = User.builder()
                .id(1)
                .name("Имя")
                .email("Почта")
                .build();
        UserDtoUpdate dtoUpdate = UserDtoUpdate.builder()
                .name("Новое имя")
                .email("Новый эмейл")
                .build();
        Mockito
                .when(userRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(user));
        assertEquals(userService.updateUser(1, dtoUpdate), UserMapper.toUserDto(user));
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        Mockito.when(userRepository.findAll()).thenReturn(users);
        assertEquals(userService.getAllUsers(), users);
    }
}