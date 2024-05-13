package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.BadEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final InMemoryUserRepository userRepository;

    public UserDto getUser(int id) {
        return UserMapper.toUserDto(userRepository.getUser(id));
    }

    public UserDto addUser(UserDto userDto) {
        User user =  UserMapper.toUser(userDto);
        userRepository.setUserCount(userRepository.getUserCount() + 1);
        user.setId(userRepository.getUserCount());
        userRepository.addUser(user.getId(), user);
        return UserMapper.toUserDto(userRepository.getUser(user.getId()));
    }

    public void removeUser(int id) {
        userRepository.removeUser(id);
    }

    public UserDto updateUser(int id, UserDtoUpdate userDto) {
        User user = userRepository.getUser(id);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (!userDto.getEmail().equals(user.getEmail())) {
                if (!checkEmail(userDto.getEmail())) {
                    user.setEmail(userDto.getEmail());
                    userRepository.addEmail(user.getId(), user.getEmail());
                } else {
                    throw new BadEmailException("Такой email уже используется");
                }
            }
        }
        userRepository.updateUser(id, user);
        return UserMapper.toUserDto(userRepository.getUser(id));
    }

    public boolean checkEmail(String email) {
        return userRepository.checkEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
