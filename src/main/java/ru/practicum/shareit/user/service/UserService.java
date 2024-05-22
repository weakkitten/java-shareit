package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.BadEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getUser(int id) {
        return UserMapper.toUserDto(userRepository.findById(id).get());
    }

    public UserDto addUser(UserDto userDto) {
        User user =  UserMapper.toUser(userDto);
        userRepository.save(user);
        return UserMapper.toUserDto(userRepository.findById(user.getId()).get());
    }

    public void removeUser(int id) {
        userRepository.deleteById(id);
    }

    public UserDto updateUser(int id, UserDtoUpdate userDto) {
        User user = userRepository.findById(id).get();
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (!userDto.getEmail().equals(user.getEmail())) {
                if (!userRepository.findByEmailContainingIgnoreCase(userDto.getEmail())) {
                    user.setEmail(userDto.getEmail());
                } else {
                    throw new BadEmailException("Такой email уже используется");
                }
            }
        }
        userRepository.save(user);
        return UserMapper.toUserDto(userRepository.findById(id).get());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
