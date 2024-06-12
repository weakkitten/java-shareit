package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapperTest {
    ItemRequest request = ItemRequest.builder()
            .description("Описание")
            .requesterId(1)
            .created(LocalDateTime.of(2024, 6, 11, 15, 16))
            .build();

    ItemRequestDto dto = ItemRequestDto.builder()
            .description("Описание")
            .created(LocalDateTime.of(2024, 6, 11, 15, 16))
            .build();

    ItemRequestDtoGet dtoGet = ItemRequestDtoGet.builder()
            .id(request.getId())
            .description(request.getDescription())
            .requesterId(request.getRequesterId())
            .created(request.getCreated())
            .build();

    RequestDtoWithListItem dtoWithListItem = RequestDtoWithListItem.builder()
            .id(request.getId())
            .description(request.getDescription())
            .created(request.getCreated())
            .items(null)
            .build();

    @Test
    void toItemRequest() {
        ItemRequest requestTest = RequestMapper.toItemRequest(dto, 1);

        Assertions.assertEquals(requestTest, request);
    }

    @Test
    void toItemRequestDtoGet() {
        ItemRequestDtoGet requestDtoGet = RequestMapper.toItemRequestDtoGet(request);

        Assertions.assertEquals(requestDtoGet, dtoGet);
    }

    @Test
    void toRequestDtoWithListItem() {
        RequestDtoWithListItem dtoWithListItemTest = RequestMapper.toRequestDtoWithListItem(request, null);

        Assertions.assertEquals(dtoWithListItemTest, dtoWithListItem);
    }
}