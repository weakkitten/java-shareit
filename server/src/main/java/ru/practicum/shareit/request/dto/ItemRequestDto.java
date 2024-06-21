package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestDto {
    @NotEmpty
    @NotBlank
    private String description;
    private LocalDateTime created;
}
