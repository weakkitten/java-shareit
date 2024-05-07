package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping
    public Item createItem(@RequestHeader("X-Sharer-User-Id") int id, @RequestBody @Valid ItemDto itemDto) {
        return service.createItem(id, itemDto);
    }

    @PatchMapping
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") int id, @RequestBody @Valid ItemDto itemDto) {
        return null;
    }

    @GetMapping
    public Item getItem(@RequestHeader("X-Sharer-User-Id") int id) {
        return null;
    }
}
