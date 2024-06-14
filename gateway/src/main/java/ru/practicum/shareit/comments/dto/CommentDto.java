package ru.practicum.shareit.comments.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentDto {
    private int id;
    @NotEmpty
    private String text;
    private int itemId;
    private int authorId;
    private LocalDateTime created;
}
