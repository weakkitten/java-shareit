package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserDtoUpdate {
    protected int id;
    protected String name;
    protected String email;
}
