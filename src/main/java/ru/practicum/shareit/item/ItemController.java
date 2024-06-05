package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.CommentGet;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGet;
import ru.practicum.shareit.item.dto.ItemDtoGetBooking;
import ru.practicum.shareit.item.service.ItemService;

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
    public ItemDtoGetBooking getItem(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return service.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoGetBooking> getAllItemByUser(@RequestHeader("X-Sharer-User-Id") int id) {
        return service.getAllUserItems(id);
    }

    @GetMapping("/search")
    public List<ItemDtoGet> searchItem(@RequestParam String text) {
        return service.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentGet createComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                    @PathVariable int itemId, @RequestBody CommentDto comment) {
        return service.createComment(userId, itemId, comment);
    }
}
