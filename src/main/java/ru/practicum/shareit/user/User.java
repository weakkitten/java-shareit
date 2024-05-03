package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;

@Data
public class User {
    @Positive
    private int id;
    private String name;
    @Email
    private String email;
}
