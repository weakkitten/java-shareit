package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Email;
@Data
public class UserDto {
    protected int id;
    protected String name;
    @Email
    protected String email;

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserDto userDto(User user) {//пока пустует
        return new UserDto();
    }
}
