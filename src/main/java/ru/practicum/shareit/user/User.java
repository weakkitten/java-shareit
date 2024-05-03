package ru.practicum.shareit.user;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;

@Data
public class User {
    @Positive
    private int id;
    private String name;
    @Email
    @UniqueElements
    private String email;
}
