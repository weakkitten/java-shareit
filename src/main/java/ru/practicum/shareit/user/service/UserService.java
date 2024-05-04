package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final InMemoryUserRepository userRepository;
    private static int userCount = 0;

    public User getUser(int id) {
        return userRepository.getUser(id);
    }

    public User addUser(User user) {
        if (user.getId() == 0) {
            userCount++;
            user.setId(userCount);
        } else {
            user.setId(user.getId() + 1);
            userCount++;
        }
        userRepository.addUser(user.getId(), user);
        return userRepository.getUser(user.getId());
    }

    public void removeUser(int id) {
        userRepository.removeUser(id);
    }

    public User updateUser(int id, User user) {
        user.setId(user.getId() + 1);
        userRepository.updateUser(id, user);
        return userRepository.getUser(id);
    }

    public boolean checkEmail(String email) {
        return userRepository.checkEmail(email);
    }
}
