package ru.practicum.shareit.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    public List<Comment> findByItem_Id(int itemId);
    public List<Comment> findByOwner_Id(int userId);
}
