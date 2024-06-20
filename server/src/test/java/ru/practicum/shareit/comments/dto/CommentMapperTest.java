package ru.practicum.shareit.comments.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comments.model.Comment;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {
    CommentDto dto = CommentDto.builder()
            .id(1)
            .text("Тест")
            .created(null)
            .build();
    Comment comment = Comment.builder()
            .id(0)
            .text("Тест")
            .created(null)
            .itemId(1)
            .authorId(1)
            .build();

    CommentGet commentGet = CommentGet.builder()
            .id(0)
            .text("Тест")
            .created(null)
            .authorName("Имя")
            .itemId(1)
            .build();

    @Test
    void toComment() {
        Comment testComment = CommentMapper.toComment(dto, 1, 1);
        comment.setCreated(testComment.getCreated());

        Assertions.assertEquals(testComment, comment);
    }

    @Test
    void toCommentGet() {
        CommentGet testCommentGet = CommentMapper.toCommentGet(comment, "Имя");
        commentGet.setCreated(testCommentGet.getCreated());

        Assertions.assertEquals(testCommentGet, commentGet);
    }
}