package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

@UtilityClass
public class RequestMapper {
    public static ItemRequest toItemRequest(ItemRequestDto dto, int userId) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .requesterId(userId)
                .created(dto.getCreated())
                .build();
    }

    public static ItemRequestDtoGet toItemRequestDtoGet(ItemRequest request) {
        return ItemRequestDtoGet.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requesterId(request.getRequesterId())
                .created(request.getCreated())
                .build();
    }

    public static RequestDtoWithListItem toRequestDtoWithListItem(ItemRequest request,
                                                                  List<ItemDtoForRequest> itemDtoForRequestList) {
        return RequestDtoWithListItem.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(itemDtoForRequestList)
                .build();
    }
}
