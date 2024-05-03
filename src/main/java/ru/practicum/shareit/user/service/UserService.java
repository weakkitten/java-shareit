package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final InMemoryUserRepository userRepository;

    public User getUser(int id) {
        return userRepository.getUser(id);
    }

    public User addUser(User user) {
        user.setId(user.getId() + 1);
        userRepository.addUser(user.getId(), user);
        return userRepository.getUser(user.getId());
    }

    public void removeUser(int id) {
        userRepository.removeUser(id);
    }

    public User updateUser(int id, User user) {
        return userRepository.updateUser(id, user);
    }

    public boolean checkEmail(String email) {
        return userRepository.checkEmail(email);
    }
}
