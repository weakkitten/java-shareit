package ru.practicum.shareit.comments.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comments.model.Comment;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static Comment toComment(CommentDto dto, int authorId, int itemId) {
        return Comment.builder()
                .text(dto.getText())
                .itemId(itemId)
                .authorId(authorId)
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentGet toCommentGet(Comment comment, String name) {
        return CommentGet.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItemId())
                .authorName(name)
                .created(comment.getCreated())
                .build();
    }
}
