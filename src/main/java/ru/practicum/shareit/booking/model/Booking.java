package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "BOOKINGS", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
        })

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "START_DATE")
    private LocalDateTime start;
    @Column(name = "END_DATE")
    private LocalDateTime end;
    private int item_id;
    private int booker_id;
    @Enumerated(EnumType.STRING)
    private Status status;
}
