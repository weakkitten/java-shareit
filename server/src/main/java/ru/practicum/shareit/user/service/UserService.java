package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserDto getUser(int id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return UserMapper.toUserDto(userRepository.findById(id).get());
    }

    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user =  UserMapper.toUser(userDto);
        userRepository.save(user);
        log.info("Пользователь с id: " + userRepository.findById(user.getId()).get().getId() + " успешно создан");
        return UserMapper.toUserDto(userRepository.findById(user.getId()).get());
    }

    @Transactional
    public boolean removeUser(int id) {
        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    public UserDto updateUser(int id, UserDtoUpdate userDto) {
        User user = userRepository.findById(id).get();
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        userRepository.save(user);
        return UserMapper.toUserDto(userRepository.findById(id).get());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
