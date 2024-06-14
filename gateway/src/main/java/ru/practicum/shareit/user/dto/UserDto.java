package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    protected int id;
    protected String name;
    @NonNull
    @Email
    @NotBlank
    protected String email;
}
