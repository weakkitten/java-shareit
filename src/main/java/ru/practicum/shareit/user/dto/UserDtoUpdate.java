package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDtoUpdate {
    protected int id;
    protected String name;
    protected String email;
}
