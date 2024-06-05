package ru.practicum.shareit.comments.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
