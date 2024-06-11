package ru.practicum.shareit.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByItemId(int itemId);

    Comment findByItemIdAndAuthorIdAndText(int itemId, int authorId, String text);

    @Query("select c, u.name " +
            "from Comment as c " +
            "join c.user as u " +
            "where c.itemId = ?1")
    List<Comment> findByIdAndUserName(int itemId);
}
