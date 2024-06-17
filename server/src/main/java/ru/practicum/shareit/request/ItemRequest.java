package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Table(name = "REQUESTS", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
        })
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    @Column(name = "REQUESTER_ID")
    private int requesterId;
    private LocalDateTime created;
}
