package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;

@Data
@Builder
public class User {
    @PositiveOrZero protected int id;
    protected String name;
    @NonNull @Email @NotBlank protected String email;
}
