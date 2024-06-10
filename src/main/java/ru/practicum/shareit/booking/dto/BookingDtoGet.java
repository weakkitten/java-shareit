package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.utility.Status;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoGet {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private BookerDto booker;
    private ItemDtoBooking item;
}
