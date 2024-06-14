package ru.practicum.shareit.comments.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Table(name = "COMMENTS", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
        })
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;
    @Column(name = "ITEM_ID")
    private int itemId;
    @Column(name = "AUTHOR_ID")
    private int authorId;
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID", insertable = false, updatable = false)
    private User user;
}
