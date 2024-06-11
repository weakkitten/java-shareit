package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {
    Item item = Item.builder()
            .id(0)
            .name("Имя")
            .description("Описание")
            .available(true)
            .owner(1)
            .request(1)
            .build();

    ItemDtoGet dtoGet = ItemDtoGet.builder()
            .id(0)
            .name("Имя")
            .description("Описание")
            .available(true)
            .requestId(1)
            .build();

    ItemDto dto = ItemDto.builder()
            .id(0)
            .name("Имя")
            .description("Описание")
            .available(true)
            .owner(1)
            .requestId(1)
            .build();

    ItemDtoGetBooking itemDtoGetBooking = ItemDtoGetBooking.builder()
            .id(0)
            .name(item.getName())
            .description(item.getDescription())
            .available(item.isAvailable())
            .lastBooking(null)
            .nextBooking(null)
            .comments(null)
            .build();

    ItemDtoForRequest itemDtoForRequest = ItemDtoForRequest.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .requestId(item.getRequest())
            .available(item.isAvailable())
            .build();

    @Test
    void toItemDtoGet() {
        ItemDtoGet testItemGet = ItemMapper.toItemDtoGet(item);

        Assertions.assertEquals(testItemGet, dtoGet);
    }

    @Test
    void toItem() {
        Item itemTest = ItemMapper.toItem(1, dto);

        Assertions.assertEquals(itemTest, item);
    }

    @Test
    void itemDtoGetBooking() {
        ItemDtoGetBooking itemDtoGetBookingTest = ItemMapper.itemDtoGetBooking(item, null, null, null);

        Assertions.assertEquals(itemDtoGetBookingTest, itemDtoGetBookingTest);
    }

    @Test
    void itemDtoForRequest() {
        ItemDtoForRequest itemDtoForRequestTest = ItemMapper.itemDtoForRequest(item);

        Assertions.assertEquals(itemDtoForRequestTest, itemDtoForRequest);
    }
}