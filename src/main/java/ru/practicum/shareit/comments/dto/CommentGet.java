package ru.practicum.shareit.comments.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentGet {
    private int id;
    private String text;
    private int itemId;
    private String authorName;
    private LocalDateTime created;
}
