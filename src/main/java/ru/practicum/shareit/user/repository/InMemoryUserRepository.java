package ru.practicum.shareit.user.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@Repository
public class InMemoryUserRepository {
    private final Map<Integer, User> userRepository = new HashMap<>();
    private final List<String> emailList = new ArrayList<>();

    public User getUser(int id) {
        return userRepository.get(id);
    }

    public void addUser(int id, User user) {
        userRepository.put(id, user);
        emailList.add(user.getEmail());
    }

    public void removeUser(int id) {
        userRepository.remove(id);
    }

    public User updateUser(int id, User user) {
        return userRepository.put(id, user);
    }

    public boolean checkEmail(String email) {
        return emailList.contains(email);
    }
}
