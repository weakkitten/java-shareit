package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGet;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDtoGet createItem(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody @Valid ItemDto itemDto) {
        return service.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoGet updateItem(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId, @RequestBody ItemDto itemDto) {
        return service.updateItem(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoGet getItem(@PathVariable int itemId) {
        return service.getItem(itemId);
    }

    @GetMapping
    public List<ItemDtoGet> getAllItemByUser(@RequestHeader("X-Sharer-User-Id") int id) {
        return service.getAllUserItems(id);
    }

    @GetMapping("/search?text={text}")
    public List<ItemDtoGet> searchItem(@PathVariable String text) {
        System.out.println(text);
        return service.searchItem(text);
    }
}
