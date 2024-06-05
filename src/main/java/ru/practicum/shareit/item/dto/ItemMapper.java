package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.comments.dto.CommentGet;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
public class ItemMapper {
    public static ItemDtoGet toItemDtoGet(Item item) {
        return new ItemDtoGet(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable()
        );
    }

    public static Item toItem(int id, ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(id)
                .build();
    }
    public static ItemDtoGetBooking itemDtoGetBooking(Item item, BookingDtoItem last, BookingDtoItem next,
                                                      List<CommentGet> commentList) {
        return ItemDtoGetBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .lastBooking(last)
                .nextBooking(next)
                .comments(commentList)
                .build();
    }
}
