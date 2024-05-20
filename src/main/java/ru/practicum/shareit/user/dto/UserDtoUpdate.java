package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserDtoUpdate {
    protected int id;
    protected String name;
    protected String email;
}
