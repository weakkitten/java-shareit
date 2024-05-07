package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
public class ItemDto {
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull//не срабатывает
    private boolean available;
    private int owner;
    private int request;

    public static Item toItem(int id, ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.isAvailable())
                .owner(id)
                .build();
    }
}
