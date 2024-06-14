package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.utility.Status;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
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
    private int itemId;
    @Column(name = "BOOKER_ID")
    private int bookerId;
    @Enumerated(EnumType.STRING)
    private Status status;
}
