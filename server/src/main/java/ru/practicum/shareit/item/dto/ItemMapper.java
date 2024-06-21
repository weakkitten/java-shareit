package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.comments.dto.CommentGet;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
public class ItemMapper {
    public static ItemDtoGet toItemDtoGet(Item item) {
        return ItemDtoGet.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .requestId(item.getRequest())
                .build();
    }

    public static Item toItem(int id, ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(id)
                .request(itemDto.getRequestId())
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

    public static ItemDtoForRequest itemDtoForRequest(Item item) {
        return ItemDtoForRequest.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .requestId(item.getRequest())
                .available(item.isAvailable())
                .build();
    }
}
