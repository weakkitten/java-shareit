package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    User user = User.builder()
            .id(0)
            .name("Имя")
            .email("user@gmail.com")
            .build();

    UserDto dto = UserDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .build();

    @Test
    void toUser() {
        User userTest = UserMapper.toUser(dto);

        Assertions.assertEquals(userTest, user);
    }

    @Test
    void toUserDto() {
        UserDto dtoTest = UserMapper.toUserDto(user);

        Assertions.assertEquals(dtoTest, dto);
    }
}