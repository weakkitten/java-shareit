package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    protected int id;
    protected String name;
    @NonNull
    @Email @NotBlank
    protected String email;
}
