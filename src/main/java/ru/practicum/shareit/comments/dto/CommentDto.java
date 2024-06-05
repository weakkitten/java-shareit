package ru.practicum.shareit.comments.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
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
