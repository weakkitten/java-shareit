package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.comments.dto.CommentGet;

import java.util.List;

@Data
@Builder
public class ItemDtoGetBooking {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoItem lastBooking;
    private BookingDtoItem nextBooking;
    private List<CommentGet> comments;
}
