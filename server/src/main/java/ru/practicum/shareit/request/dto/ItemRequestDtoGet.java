package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestDtoGet {
    private int id;
    private String description;
    private int requesterId;
    private LocalDateTime created;
}
